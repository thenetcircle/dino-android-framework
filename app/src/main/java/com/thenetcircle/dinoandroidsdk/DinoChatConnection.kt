package com.thenetcircle.dinoandroidsdk

import android.support.annotation.NonNull
import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.thenetcircle.dinoandroidsdk.model.data.ChannelListModel
import com.thenetcircle.dinoandroidsdk.model.data.LoginModel
import com.thenetcircle.dinoandroidsdk.model.data.RoomListModel
import com.thenetcircle.dinoandroidsdk.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidsdk.model.results.LoginModelResult
import com.thenetcircle.dinoandroidsdk.model.results.ModelResultParent
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
    private val connectionURL: String
    private val connectionListener: DinoConnectionListener
    private var socket: Socket? = null
    private var loggedIn: Boolean? = false

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

    private inline fun <reified T : ModelResultParent> processResult(@NonNull data: String): T? {
        val model = gson.fromJson<T>(data, T::class.java)
        if (model != null && model.statusCode == 200) {
            return model
        } else {
            connectionListener.onError(if (model != null) DinoError.getErrorByCode(model.statusCode!!) else DinoError.UNKNOWN_ERROR)
            disconnect()
        }
        return null
    }


    fun login(loginModel: LoginModel) {
        if (socket == null) {
            connectionListener.onError(DinoError.NO_SOCKET_ERROR)
            return
        }
        socket!!.on("gn_login") { args ->
            if (args.isNotEmpty()) {
                socket!!.off("gn_login")
                val model = processResult<LoginModelResult>(args[0].toString())
                if (model != null) {
                    loggedIn = true
                    connectionListener.onResult(model)
                }
            } else {
                connectionListener.onError(DinoError.UNKNOWN_ERROR)
                disconnect()
            }
        }

        socket!!.emit("login", JSONObject(gson.toJson(loginModel)))
    }

    fun getChannelList(channelListModel: ChannelListModel) {
        if (!generalChecks()) {
            return
        }
        socket!!.on("gn_list_channels") { args ->
            socket!!.off("gn_list_channels")
            if (args.isNotEmpty()) {
                val model = processResult<ChannelListModelResult>(args[0].toString())
                if (model != null) {
                    connectionListener.onResult(model)
                }
            } else {
                connectionListener.onError(DinoError.UNKNOWN_ERROR)
                disconnect()
            }
        }

        socket!!.emit("list_channels", JSONObject(gson.toJson(channelListModel)))
    }

    fun getRoomList(roomListModel: RoomListModel) {
        if (!generalChecks()) {
            return
        }
        socket!!.on("gn_list_rooms") { args ->
            socket!!.off("gn_list_rooms")
            if (args.isNotEmpty()) {
                val model = processResult<RoomListModelResult>(args[0].toString())
                if (model != null) {
                    connectionListener.onResult(model)
                }
            } else {
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

    private fun generalChecks(): Boolean {
        if (socket == null) {
            connectionListener.onError(DinoError.NO_SOCKET_ERROR)
            return false
        }

        if (loggedIn == false) {
            connectionListener.onError(DinoError.LOCAL_NOT_LOGGED_IN)
            return false
        }

        return true
    }

    private fun connectNewSocket(url: String): Socket {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        opts.forceNew = true
        return IO.socket(url, opts)
    }
}
