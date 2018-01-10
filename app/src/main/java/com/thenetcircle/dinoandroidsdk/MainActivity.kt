package com.thenetcircle.dinoandroidsdk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.thenetcircle.dinoandroidsdk.model.data.ChannelListModel
import com.thenetcircle.dinoandroidsdk.model.data.LoginModel
import com.thenetcircle.dinoandroidsdk.model.data.RoomListModel
import com.thenetcircle.dinoandroidsdk.model.results.ChannelAttachment
import com.thenetcircle.dinoandroidsdk.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidsdk.model.results.LoginModelResult
import com.thenetcircle.dinoandroidsdk.model.results.RoomListModelResult

class MainActivity : AppCompatActivity(), DinoConnectionListener {

    var dinoChatConnection : DinoChatConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dinoChatConnection = DinoChatConnection("http://10.60.1.124:9210/ws", this)
        dinoChatConnection!!.startConnection()
    }

    override fun onConnection() {
        val loginModel = LoginModel(179677, "Aaron", "881832c408f599617aaa8da306a36f4a36cb1611")
        dinoChatConnection!!.login(loginModel)
    }

    override fun onError(error: DinoError) {
        Log.e("Error", error.toString())
        dinoChatConnection!!.disconnect()
    }

    override fun onLogin(loginModelResult: LoginModelResult) {
        dinoChatConnection!!.getChannelList(ChannelListModel())
    }

    override fun onChannelListReceived(channelListModelResult: ChannelListModelResult) {
        for (ob: ChannelAttachment  in channelListModelResult.data!!.objectX.attachments) {
            Log.d("onChannelListReceived", ob.objectType)
            if(ob.objectType.toLowerCase() != "temporary") {
                dinoChatConnection!!.getRoomList(RoomListModel(ob.id))
            }
        }
    }

    override fun onChannelRoomReceived(roomListModelResult: RoomListModelResult) {
        Log.e("onChannelRoomReceived", roomListModelResult.statusCode.toString());
    }
}
