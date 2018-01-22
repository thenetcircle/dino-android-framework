/*
 * Copyright 2018 The NetCircle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thenetcircle.dino

import android.os.Handler
import android.os.Looper
import android.support.annotation.NonNull
import com.google.gson.GsonBuilder
import com.thenetcircle.dino.interfaces.*
import com.thenetcircle.dino.model.data.*
import com.thenetcircle.dino.model.results.LoginModelResult
import com.thenetcircle.dino.model.results.ModelResultParent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject


/**
 * Created by aaron on 09/01/2018.
 */
class DinoChatConnection {
    private val gson = GsonBuilder().disableHtmlEscaping().create()
    private var socket: Socket? = null
    var isLoggedIn: Boolean = false
    var isConnected: Boolean = false
        get() = if (socket != null) socket!!.connected() else false

    var connectionListener: DinoConnectionListener? = null
    var messageReceivedListener : DinoMessageReceivedListener? = null

    fun startConnection(url: String, @NonNull errorListener: DinoErrorListener) {
        startConnection(connectNewSocket(url), errorListener)
    }

    fun startConnection(newSocket: Socket, @NonNull errorListener: DinoErrorListener) {
        if (connectionListener == null) {
            throw IllegalArgumentException("DinoConnectionListener must be set")
        }

        socket = newSocket

        socket!!.on(Socket.EVENT_CONNECT_ERROR) {
            Handler(Looper.getMainLooper()).post({ errorListener.onError(DinoError.EVENT_CONNECT_ERROR) })
            connectionListener?.onDisconnect()
        }

        socket!!.on(Socket.EVENT_DISCONNECT) {
            Handler(Looper.getMainLooper()).post({ errorListener.onError(DinoError.EVENT_DISCONNECT) })
            connectionListener?.onDisconnect()
        }

        socket!!.on("gn_connect") {
            socket!!.off("gn_connect")
            if(Looper.getMainLooper() == Looper.myLooper()) {
                connectionListener?.onConnect()
            } else {
                Handler(Looper.getMainLooper()).post({ connectionListener?.onConnect() })
            }
        }
        socket!!.connect()
    }

    private inline fun <reified T : ModelResultParent> processResult(@NonNull data: String, @NonNull listener: DinoParentInterface<T>, @NonNull errorListener: DinoErrorListener): Boolean {
        val model = gson.fromJson<T>(data, T::class.java)
        if (model != null && model.statusCode == 200) {
            Handler(Looper.getMainLooper()).post({ listener.onResult(model) })
            return true
        } else {
            val error = if (model != null) DinoError.getErrorByCode(model.statusCode!!) else DinoError.UNKNOWN_ERROR
            Handler(Looper.getMainLooper()).post({ errorListener.onError(error) })
        }
        return false
    }


    private inline fun <D : Any, reified R : ModelResultParent> processRequest(@NonNull requestEvent: String, @NonNull responseEvent: String, @NonNull dataModel: D, @NonNull listener: DinoParentInterface<R>, @NonNull errorListener: DinoErrorListener) {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }
        socket!!.on(responseEvent) { args ->
            if (args.isNotEmpty()) {
                socket!!.off(responseEvent)
                processResult(args[0].toString(), listener, errorListener)
            } else {
                errorListener.onError(DinoError.UNKNOWN_ERROR)
            }
        }

        socket!!.emit(requestEvent, JSONObject(gson.toJson(dataModel)))
    }

    fun login(loginModel: LoginModel, @NonNull loginListener: DinoLoginListener, @NonNull errorListener: DinoErrorListener) {

        processRequest("login", "gn_login", loginModel, object : DinoLoginListener {
            override fun onResult(result: LoginModelResult) {
                isLoggedIn = true
                loginListener.onResult(result)
            }
        }, errorListener)
    }

    fun getChannelList(channelListModel: ChannelListModel, @NonNull channelListListener: DinoChannelListListener, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        processRequest("list_channels", "gn_list_channels", channelListModel, channelListListener, errorListener)
    }

    fun getRoomList(roomListModel: RoomListModel, @NonNull roomEntryListener: DinoRoomEntryListener, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        processRequest("list_rooms", "gn_list_rooms", roomListModel, roomEntryListener, errorListener)
    }

    fun createPrivateRoom(privateModel: CreateRoomPrivateModel, @NonNull roomCreationListener: DinoRoomCreationListener, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        processRequest("create", "gn_create", privateModel, roomCreationListener, errorListener)
    }

    fun joinRoom(joinModel: JoinRoomModel, @NonNull joinRoomListener: DinoJoinRoomListener, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        processRequest("join", "gn_join", joinModel, joinRoomListener, errorListener)
    }

    fun getChatRoomHistory(chatHistory: ChatHistory, @NonNull dinoChatHistoryListener: DinoChatHistoryListener, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        processRequest("history", "gn_history", chatHistory, dinoChatHistoryListener, errorListener)
    }

    fun sendMessage(chatSendMessage: ChatSendMessage, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }
        socket!!.emit("message", JSONObject(gson.toJson(chatSendMessage)))
    }

    fun registerMessageListener(@NonNull errorListener: DinoErrorListener) {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }

        socket!!.on("message") { args ->
            Handler(Looper.getMainLooper()).post({
                processResult(args[0].toString(), messageReceivedListener!!, errorListener)
            })
        }
    }

    fun leaveRoom(leaveRoomModel: LeaveRoomModel, @NonNull errorListener: DinoErrorListener) {
        generalChecks(errorListener)
        socket!!.emit("leave", JSONObject(gson.toJson(leaveRoomModel)))
    }

    fun unRegisterMessageListener() {
        if (socket == null) {
            return
        }
        socket!!.off("message")
    }

    fun disconnect() {
        if (socket != null) {
            socket!!.off(Socket.EVENT_DISCONNECT)
            socket!!.off(Socket.EVENT_CONNECT_ERROR)
            socket!!.disconnect()
            socket = null
        }
    }

    private fun generalChecks(@NonNull connectionListener: DinoErrorListener): Boolean {
        if (socket == null) {
            connectionListener.onError(DinoError.NO_SOCKET_ERROR)
            return false
        }

        if (isLoggedIn == false) {
            connectionListener.onError(DinoError.LOCAL_NOT_LOGGED_IN)
            return false
        }
        return true
    }

    fun connectNewSocket(url: String): Socket {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        opts.forceNew = true
        return IO.socket(url, opts)
    }
}

