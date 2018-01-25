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
import com.thenetcircle.dino.model.results.MessageReceived
import com.thenetcircle.dino.model.results.ModelResultParent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject


/**
 * Created by aaron on 09/01/2018.
 */
class DinoChatConnection {
    private val gson = GsonBuilder().disableHtmlEscaping().create()
    private var socket: Socket? = null

    var currentLoggedInUser: LoginModelResult? = null

    var isLoggedIn: Boolean = false
        get() = currentLoggedInUser != null

    var isConnected: Boolean = false
        get() = if (socket != null) socket!!.connected() else false

    var connectionListener: DinoConnectionListener? = null
    var messageReceivedListener: DinoMessageReceivedListener? = null

    var dinoConfig: DinoConfig

    constructor() {
        this.dinoConfig = DinoConfig()
    }

    constructor(dinoConfig: DinoConfig) {
        this.dinoConfig = dinoConfig
    }

    fun startConnection(url: String, @NonNull errorListener: DinoErrorListener) {
        startConnection(connectNewSocket(url), errorListener)
    }

    fun startConnection(newSocket: Socket, @NonNull errorListener: DinoErrorListener) {
        if (connectionListener == null) {
            throw IllegalArgumentException("DinoConnectionListener must be set")
        }
        socket = newSocket

        socket!!.on(Socket.EVENT_CONNECT_ERROR) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                errorListener.onError(DinoError.EVENT_CONNECT_ERROR)
                connectionListener?.onDisconnect()
            } else {
                Handler(Looper.getMainLooper()).post({
                    errorListener.onError(DinoError.EVENT_CONNECT_ERROR)
                    connectionListener?.onDisconnect()
                })
            }
        }

        socket!!.on(Socket.EVENT_DISCONNECT) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                errorListener.onError(DinoError.EVENT_DISCONNECT)
                connectionListener?.onDisconnect()
            } else {
                Handler(Looper.getMainLooper()).post({
                    errorListener.onError(DinoError.EVENT_DISCONNECT)
                    connectionListener?.onDisconnect()
                })
            }

        }

        socket!!.on("gn_connect") {
            off("gn_connect")
            if (Looper.getMainLooper() == Looper.myLooper()) {
                connectionListener?.onConnect()
            } else {
                Handler(Looper.getMainLooper()).post({ connectionListener?.onConnect() })
            }
        }
        socket!!.connect()
    }

    private fun off(event: String) {
        if (socket != null) {
            socket!!.off(event)
        }
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
                off(responseEvent)
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
                currentLoggedInUser = result
                loginListener.onResult(result)
            }
        }, errorListener)
    }

    fun getChannelList(channelListModel: ChannelListModel, @NonNull channelListListener: DinoChannelListListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("list_channels", "gn_list_channels", channelListModel, channelListListener, errorListener)
    }

    fun getRoomList(roomListModel: RoomListModel, @NonNull roomEntryListener: DinoRoomEntryListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("list_rooms", "gn_list_rooms", roomListModel, roomEntryListener, errorListener)
    }

    fun createPrivateRoom(privateModel: CreateRoomPrivateModel, @NonNull roomCreationListener: DinoRoomCreationListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("create", "gn_create", privateModel, roomCreationListener, errorListener)
    }

    fun joinRoom(joinModel: JoinRoomModel, @NonNull joinRoomListener: DinoJoinRoomListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("join", "gn_join", joinModel, joinRoomListener, errorListener)
    }

    fun getChatRoomHistory(chatHistory: ChatHistory, @NonNull dinoChatHistoryListener: DinoChatHistoryListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("history", "gn_history", chatHistory, dinoChatHistoryListener, errorListener)
    }


    fun sendMessageResponseReceived(@NonNull deliveryReceiptModel: DeliveryReceiptModel, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("received", JSONObject(gson.toJson(deliveryReceiptModel)))
    }

    fun sendMessageResponseRead(@NonNull deliveryReceiptModel: DeliveryReceiptModel, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("read", JSONObject(gson.toJson(deliveryReceiptModel)))
    }

    fun sendMessage(chatSendMessage: ChatSendMessage, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("message", JSONObject(gson.toJson(chatSendMessage)))
    }

    fun registerMessageSentListener(@NonNull dinoMessageReceivedListener: DinoChatMessageListener, @NonNull errorListener: DinoErrorListener) {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }
        socket!!.on("gn_message") { args ->
            Handler(Looper.getMainLooper()).post({
                processResult(args[0].toString(), dinoMessageReceivedListener, errorListener)
            })
        }
    }

    fun unRegisterMessageSentListener() {
        if (socket == null) {
            return
        }
        socket!!.off("gn_message")
    }

    fun registerMessageListener(@NonNull errorListener: DinoErrorListener) {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }

        socket!!.on("message") { args ->
            Handler(Looper.getMainLooper()).post({
                processResult(args[0].toString(), object : DinoParentInterface<MessageReceived> {
                    override fun onResult(result: MessageReceived) {
                        if (dinoConfig.autoSendMessageReceivedACK) {
                            if (result.actor?.id != currentLoggedInUser?.data?.actor?.id) {
                                val roomID = result.target?.id
                                val delModel = DeliveryReceiptModel(DeliveryReceiptModel.DeliveryState.RECEIVED, roomID!!, DeliveryReceiptModel.DeliveryEntry(result.id!!))
                                sendMessageResponseReceived(delModel, errorListener)
                            }
                        }
                        //send result to listener
                        messageReceivedListener!!.onResult(result)
                    }
                }, errorListener)
            })
        }
    }

    fun registerMessageStatusUpdateListener(@NonNull listener: DinoMessageStatusUpdateListener, @NonNull errorListener: DinoErrorListener) {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }

        val emitter = Emitter.Listener { args ->
            processResult(args[0].toString(), listener, errorListener)
        }

        socket!!.on("gn_message_read", emitter)
        socket!!.on("gn_message_received", emitter)
    }

    fun unRegisterMessageStatusUpdateListener() {
        if (socket == null) {
            return
        }
        socket!!.off("gn_message_read")
        socket!!.off("gn_message_received")
    }

    fun leaveRoom(leaveRoomModel: LeaveRoomModel, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
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
            currentLoggedInUser = null
            socket = null
        }
    }

    fun generalChecks(@NonNull connectionListener: DinoErrorListener): Boolean {
        if (socket == null) {
            connectionListener.onError(DinoError.NO_SOCKET_ERROR)
            return false
        }

        if (!isLoggedIn) {
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

