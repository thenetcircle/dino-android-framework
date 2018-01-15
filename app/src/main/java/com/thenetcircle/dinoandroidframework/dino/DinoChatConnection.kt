package com.thenetcircle.dinoandroidframework.dino

import android.os.Handler
import android.os.Looper
import android.support.annotation.NonNull
import com.google.gson.GsonBuilder
import com.thenetcircle.dinoandroidframework.dino.interfaces.*
import com.thenetcircle.dinoandroidframework.dino.model.data.ChannelListModel
import com.thenetcircle.dinoandroidframework.dino.model.data.LoginModel
import com.thenetcircle.dinoandroidframework.dino.model.data.RoomListModel
import com.thenetcircle.dinoandroidframework.dino.model.results.LoginModelResult
import com.thenetcircle.dinoandroidframework.dino.model.results.ModelResultParent
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

    fun startConnection(url: String, @NonNull connectionListener: DinoConnectionListener, @NonNull errorListener: DinoErrorListener) {

        socket = connectNewSocket(url)
        socket!!.on(Socket.EVENT_CONNECT_ERROR) { errorListener.onError(DinoError.EVENT_CONNECT_ERROR) }
        socket!!.on(Socket.EVENT_DISCONNECT) { errorListener.onError(DinoError.EVENT_DISCONNECT) }
        socket!!.on("gn_connect") {
            socket!!.off("gn_connect")
            connectionListener.onConnect()
        }
        socket!!.connect()
    }

    private inline fun <reified T : ModelResultParent> processResult(@NonNull data: String, @NonNull listener: DinoParentInterface<T>, @NonNull errorListener: DinoErrorListener): Boolean {
        val model = gson.fromJson<T>(data, T::class.java)
        if (model != null && model.statusCode == 200) {
            Handler(Looper.getMainLooper()).post({listener.onResult(model)})
            return true
        } else {
            val error = if (model != null) DinoError.getErrorByCode(model.statusCode!!) else DinoError.UNKNOWN_ERROR
            Handler(Looper.getMainLooper()).post({errorListener.onError(error)})
        }
        return false
    }


    private inline fun <D : Any, reified R : ModelResultParent> processRequest(@NonNull requestEvent:String, @NonNull responseEvent:String, @NonNull dataModel: D, @NonNull listener: DinoParentInterface<R>, @NonNull errorListener: DinoErrorListener) {
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

        processRequest("login", "gn_login", loginModel, object:DinoLoginListener
        {
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

    private fun connectNewSocket(url: String): Socket {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        opts.forceNew = true
        return IO.socket(url, opts)
    }
}

