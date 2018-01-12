package com.thenetcircle.dinoandroidsdk

import android.os.Bundle
import android.util.Log
import com.thenetcircle.dinoandroidsdk.dino.DinoError
import com.thenetcircle.dinoandroidsdk.dino.interfaces.DinoChannelListListener
import com.thenetcircle.dinoandroidsdk.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidsdk.dino.model.data.ChannelListModel
import com.thenetcircle.dinoandroidsdk.dino.model.results.ChannelAttachment
import com.thenetcircle.dinoandroidsdk.dino.model.results.ChannelListModelResult

/**
 * Created by aaron on 11/01/2018.
 */
class TNCChannelList : TNCBaseActivity(), DinoChannelListListener, DinoErrorListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)
    }

    override fun onResume() {
        super.onResume()
        dinoChatConnection.getChannelList(ChannelListModel(), this, this)

    }

    override fun onError(error: DinoError) {
        dinoChatConnection.disconnect()
    }

    override fun onResult(channelListModelResult: ChannelListModelResult) {
        for (ob: ChannelAttachment in channelListModelResult.data!!.objectX.attachments) {
            Log.d("onChannelListReceived", ob.objectType)
            // if (ob.objectType.toLowerCase() != "temporary") {
            //    dinoChatConnection.getRoomList(RoomListModel(ob.id), this)
            //}
        }
    }
}