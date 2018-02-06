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
 * A group of DinoChatConnection*.
 *
 * A wrapper class for Dino Connections to a selected server
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

    /**
     * default constructor using default configuration
     */
    constructor() {
        this.dinoConfig = DinoConfig()
    }

    /**
     * Secondary constructor containing a user defined DinoConfig
     *
     * @param dinoConfig user defined DinoConfig
     */
    constructor(dinoConfig: DinoConfig) {
        this.dinoConfig = dinoConfig
    }


    /**
     * Create a connection to a server with a given URL
     *
     * @param url server url
     * @param errorListener error callback reference,
     * @exception IllegalArgumentException if connectionListener is not set
     */
    fun startConnection(url: String, @NonNull errorListener: DinoErrorListener) {
        startConnection(connectNewSocket(url), errorListener)
    }

    /**
     * Create a connection to a server with a user generated socket.io socket
     *
     * @param newSocket socket.io socket
     * @param errorListener error callback reference
     * @exception IllegalArgumentException if connectionListener is not set
     */
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

        socket!!.on("gn_connect", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                off("gn_connect", this)
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    connectionListener?.onConnect()
                } else {
                    Handler(Looper.getMainLooper()).post({ connectionListener?.onConnect() })
                }
            }
        })
        socket!!.connect()
    }

    /**
     * remove emitter listener from socket
     *
     * @param event event name
     */
    private fun off(event: String, emitter: Emitter.Listener) {
        if (socket != null) {
            socket!!.off(event, emitter)
        }
    }

    private inline fun <reified T : ModelResultParent> processResult(@NonNull data: String, @NonNull listener: DinoParentInterface<T>, @NonNull errorListener: DinoErrorListener): Boolean {
        val model = gson.fromJson<T>(data, T::class.java)
        if (model != null && model.statusCode == 200) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                listener.onResult(model)
            } else {
                Handler(Looper.getMainLooper()).post({ listener.onResult(model) })
            }
            return true
        } else {
            val error = if (model != null) DinoError.getErrorByCode(model.statusCode!!) else DinoError.UNKNOWN_ERROR
            if (Looper.getMainLooper() == Looper.myLooper()) {
                errorListener.onError(error)
            } else {
                Handler(Looper.getMainLooper()).post({ errorListener.onError(error) })
            }
        }
        return false
    }

    private inline fun <D : Any, reified R : ModelResultParent> processRequest(@NonNull requestEvent: String, @NonNull responseEvent: String, @NonNull dataModel: D, @NonNull listener: DinoParentInterface<R>, @NonNull errorListener: DinoErrorListener) {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }
        socket!!.on(responseEvent, object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                if (args.isNotEmpty()) {
                    off(responseEvent, this)
                    processResult(args[0].toString(), listener, errorListener)
                } else {
                    errorListener.onError(DinoError.UNKNOWN_ERROR)
                }
            }
        })

        socket!!.emit(requestEvent, JSONObject(gson.toJson(dataModel)))
    }

    /**
     *login process for a dino connection
     *
     * @param loginModel LoginModel object containing required credentials
     * @param loginListener success listener
     * @param errorListener fail listener
     */
    fun login(loginModel: LoginModel, @NonNull loginListener: DinoLoginListener, @NonNull errorListener: DinoErrorListener) {
        processRequest("login", "gn_login", loginModel, object : DinoLoginListener {
            override fun onResult(result: LoginModelResult) {
                currentLoggedInUser = result
                loginListener.onResult(result)
            }
        }, errorListener)
    }

    /**
     * get all channels visible to a user
     *
     * @param channelListModel request model
     * @param channelListListener success listener
     * @param errorListener fail listener
     */
    fun getChannelList(channelListModel: RequestChannelList, @NonNull channelListListener: DinoChannelListListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("list_channels", "gn_list_channels", channelListModel, channelListListener, errorListener)
    }

    /**
     * get rooms for a given channel
     *
     * @param roomListModel request model
     * @param roomEntryListener success listener
     * @param errorListener fail listener
     */
    fun getRoomList(roomListModel: RequestRoomList, @NonNull roomEntryListener: DinoRoomEntryListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("list_rooms", "gn_list_rooms", roomListModel, roomEntryListener, errorListener)
    }

    /**
     * allow for creating a private room within a given channel
     *
     * @param privateModel request model
     * @param roomCreationListener success listener
     * @param errorListener fail listener
     */
    fun createPrivateRoom(privateModel: CreateRoomPrivateModel, @NonNull roomCreationListener: DinoRoomCreationListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("create", "gn_create", privateModel, roomCreationListener, errorListener)
    }

    /**
     * request to join a selected chat room
     *
     * @param joinModel request model
     * @param joinRoomListener success listener
     * @param errorListener fail listener
     */
    fun joinRoom(joinModel: JoinRoomModel, @NonNull joinRoomListener: DinoJoinRoomListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("join", "gn_join", joinModel, joinRoomListener, errorListener)
    }

    /**
     * get history of a chat room
     *
     * @param chatHistory request model
     * @param dinoChatHistoryListener success listener
     * @param errorListener fail listener
     */
    fun getChatRoomHistory(chatHistory: RequestChatHistoryModel, @NonNull dinoChatHistoryListener: DinoChatHistoryListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("history", "gn_history", chatHistory, dinoChatHistoryListener, errorListener)
    }


    /**
     * confirm that you have received a user sent message from dino, not have not yet read.
     *
     * @param deliveryReceiptModel delivery model
     * @param errorListener fail listener
     */
    fun sendMessageResponseReceived(@NonNull deliveryReceiptModel: SendDeliveryReceiptModel, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("received", JSONObject(gson.toJson(deliveryReceiptModel)))
    }

    /**
     * confirm that a user sent message has been read
     *
     * @param deliveryReceiptModel delivery model
     * @param errorListener fail listener
     */
    fun sendMessageResponseRead(@NonNull deliveryReceiptModel: SendDeliveryReceiptModel, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("read", JSONObject(gson.toJson(deliveryReceiptModel)))
    }

    /**
     * send a message to a user / room
     *
     * @param chatSendMessage message model
     * @param errorListener fail listener
     */
    fun sendMessage(chatSendMessage: ChatSendMessage, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("message", JSONObject(gson.toJson(chatSendMessage)))
    }

    /**
     * Get Status of messages already sent
     *
     * @param messageStatusModel request model
     * @param MessageStatusRequestListener success listener
     * @param errorListener fail listener
     */
    fun getStatusHistory(messageStatusModel: RequestMessageStatusModel, @NonNull MessageStatusRequestListener: DinoMessageStatusRequestListener, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        processRequest("msg_status", "gn_msg_status", messageStatusModel, MessageStatusRequestListener, errorListener)
    }

    /**
     * register a listener to receive sendMessage() server acknowledgments
     *
     * @param dinoMessageReceivedListener acknowledgment listener
     * @param errorListener fail listener
     */
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

    /**
     * remove listener to receive sendMessage() server acknowledgments, cancels registerMessageSentListener()
     */
    fun unRegisterMessageSentListener() {
        if (socket == null) {
            return
        }
        socket!!.off("gn_message")
    }


    /**
     * register a listener for user sent messages.
     *
     * @param errorListener fail listener
     */
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
                            if (result.actor?.id != currentLoggedInUser?.data?.loginActor?.id) {
                                val roomID = result.target?.id
                                val delModel = SendDeliveryReceiptModel(SendDeliveryReceiptModel.DeliveryState.RECEIVED, roomID!!, SendDeliveryReceiptModel.DeliveryEntry(result.id!!))
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

    /**
     * remove listener for user messages, cancels registerMessageListener()
     */
    fun unRegisterMessageListener() {
        if (socket == null) {
            return
        }
        socket!!.off("message")
    }

    /**
     * register a listener for status changes for user messages.
     *
     * @param listener status listener
     * @param errorListener fail listener
     */
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

    /**
     * remove listener for status changes for user messages. cancels registerMessageStatusUpdateListener()
     */
    fun unRegisterMessageStatusUpdateListener() {
        if (socket == null) {
            return
        }
        socket!!.off("gn_message_read")
        socket!!.off("gn_message_received")
    }

    /**
     * leave a room you have previously joined
     *
     * @param leaveRoomModel request model
     * @param errorListener fail listener
     */
    fun leaveRoom(leaveRoomModel: LeaveRoomModel, @NonNull errorListener: DinoErrorListener) {
        if (!generalChecks(errorListener)) {
            return
        }
        socket!!.emit("leave", JSONObject(gson.toJson(leaveRoomModel)))
    }

    /**
     * disconnect current socket
     */
    fun disconnect() {
        if (socket != null) {
            socket!!.off(Socket.EVENT_DISCONNECT)
            socket!!.off(Socket.EVENT_CONNECT_ERROR)
            socket!!.disconnect()
            currentLoggedInUser = null
            socket = null
        }
    }

    /**
     * check that a request is valid before processing
     *
     * @param errorListener fail listener
     */
    fun generalChecks(@NonNull errorListener: DinoErrorListener): Boolean {
        if (socket == null) {
            errorListener.onError(DinoError.NO_SOCKET_ERROR)
            return false
        }

        if (!isLoggedIn) {
            errorListener.onError(DinoError.LOCAL_NOT_LOGGED_IN)
            return false
        }
        return true
    }

    /**
     * create default socket.io socket
     *
     * @param URL of server
     * @return Socket to be used for connection
     */
    fun connectNewSocket(url: String): Socket {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        opts.forceNew = true
        return IO.socket(url, opts)
    }
}

