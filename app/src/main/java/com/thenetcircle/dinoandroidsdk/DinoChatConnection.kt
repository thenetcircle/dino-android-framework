package com.thenetcircle.dinoandroidsdk

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.thenetcircle.dinoandroidsdk.model.data.ChannelListModel
import com.thenetcircle.dinoandroidsdk.model.data.LoginModel
import com.thenetcircle.dinoandroidsdk.model.data.RoomListModel
import com.thenetcircle.dinoandroidsdk.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidsdk.model.results.LoginModelResult
import com.thenetcircle.dinoandroidsdk.model.results.RoomListModelResult
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import java.lang.IllegalArgumentException


/**
 * Created by aaron on 09/01/2018.
 */
class DinoChatConnection(url: String, listener: DinoConnectionListener) {
    private val gson = GsonBuilder().disableHtmlEscaping().create()
    val connectionURL : String
    val connectionListener : DinoConnectionListener
    var socket: Socket? = null
    var loggedIn : Boolean? = false

    init {
        if (TextUtils.isEmpty(url)) {
            throw IllegalArgumentException("URL is empty")
        }
        connectionURL = url
        connectionListener = listener
    }

    fun startConnection() {
        socket = connectNewSocket(connectionURL)
        socket!!.on(Socket.EVENT_CONNECT_ERROR) { connectionListener.onError(DinoError.EVENT_CONNECT_ERROR) }
        socket!!.on(Socket.EVENT_DISCONNECT) { connectionListener.onError(DinoError.EVENT_DISCONNECT) }
        socket!!.on("gn_connect") {
            socket!!.off("gn_connect")
            connectionListener.onConnection()
        }
        socket!!.connect()
    }

    fun login(loginModel: LoginModel) {
        if (socket == null) {
            connectionListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }

        socket!!.on("gn_login") {
            args ->
            if(args.isNotEmpty()) {
                socket!!.off("gn_login")
                val loginResultModel = gson.fromJson<LoginModelResult>(args[0].toString(), LoginModelResult::class.java)
                if (loginResultModel != null && loginResultModel.statusCode == 200) {
                    loggedIn = true
                    connectionListener.onLogin(loginResultModel)
                } else if (loginResultModel != null) {
                    connectionListener.onError(DinoError.getErrorByCode(loginResultModel.statusCode!!))
                    disconnect()
                } else {
                    connectionListener.onError(DinoError.UNKNOWN_ERROR)
                    disconnect()
                }
            } else {
                connectionListener.onError(DinoError.UNKNOWN_ERROR)
                disconnect()
            }
        }

        socket!!.emit("login", JSONObject(gson.toJson(loginModel)))
    }

    fun getChannelList(channelListModel: ChannelListModel) {
        if(!generalChecks()) {
            return
        }

        socket!!.on("gn_list_channels") {
            args ->
            socket!!.off("gn_list_channels")
            if(args.isNotEmpty()) {
                val channelListModelResult = gson.fromJson<ChannelListModelResult>(args[0].toString(), ChannelListModelResult::class.java)
                if(channelListModelResult != null && channelListModelResult.statusCode == 200) {
                    connectionListener.onChannelListReceived(channelListModelResult)
                } else if (channelListModelResult != null) {
                    connectionListener.onError(DinoError.getErrorByCode(channelListModelResult.statusCode!!))
                    disconnect()
                } else {
                    connectionListener.onError(DinoError.UNKNOWN_ERROR)
                    disconnect()
                }
            }
            else {
                connectionListener.onError(DinoError.UNKNOWN_ERROR)
                disconnect()
            }
        }

        socket!!.emit("list_channels", JSONObject(gson.toJson(channelListModel)))
    }

    fun getRoomList(roomListModel: RoomListModel) {
        if(!generalChecks()) {
            return
        }

        socket!!.on("gn_list_rooms") {
            args ->
            socket!!.off("gn_list_rooms")
            if(args.isNotEmpty()) {
                val roomModelResult = gson.fromJson<RoomListModelResult>(args[0].toString(), RoomListModelResult::class.java)
                if(roomModelResult != null && roomModelResult.statusCode == 200) {
                    connectionListener.onChannelRoomReceived(roomModelResult)
                } else if (roomModelResult != null) {
                    connectionListener.onError(DinoError.getErrorByCode(roomModelResult.statusCode!!))
                    disconnect()
                } else {
                    connectionListener.onError(DinoError.UNKNOWN_ERROR)
                    disconnect()
                }
            }
            else {
                connectionListener.onError(DinoError.UNKNOWN_ERROR)
                disconnect()
            }
        }

        socket!!.emit("list_rooms", JSONObject(gson.toJson(roomListModel)))
    }

    fun disconnect() {
        if (socket != null) {
            socket!!.off(Socket.EVENT_DISCONNECT)
            socket!!.disconnect()
        }
    }

    private fun generalChecks() : Boolean {
        if (socket == null) {
            connectionListener.onError(DinoError.NO_SOCKET_ERROR)
            return false
        }

        if(loggedIn == false) {
            connectionListener.onError(DinoError.NOT_LOGGED_IN)
            return false
        }

        return true
    }

    private fun connectNewSocket(url: String) : Socket {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        opts.forceNew = true
        return IO.socket(url,opts)
    }
}