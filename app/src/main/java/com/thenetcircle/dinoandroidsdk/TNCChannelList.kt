package com.thenetcircle.dinoandroidsdk

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.thenetcircle.dinoandroidsdk.dino.DinoError
import com.thenetcircle.dinoandroidsdk.dino.interfaces.DinoChannelListListener
import com.thenetcircle.dinoandroidsdk.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidsdk.dino.model.data.ChannelListModel
import com.thenetcircle.dinoandroidsdk.dino.model.results.ChannelAttachment
import com.thenetcircle.dinoandroidsdk.dino.model.results.ChannelListModelResult
import kotterknife.bindView
import java.nio.charset.Charset

/**
 * Created by aaron on 11/01/2018.
 */
class TNCChannelList : TNCBaseActivity(), DinoChannelListListener, DinoErrorListener{

    private val channelContainer : LinearLayout by bindView(R.id.channel_list_container)
    private val progressBar: ProgressBar by bindView(R.id.progress_wheel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        dinoChatConnection.getChannelList(ChannelListModel(), this, this)

    }

    override fun onError(error: DinoError) {
        dinoChatConnection.disconnect()
    }

    override fun onResult(result: ChannelListModelResult) {
        progressBar.visibility = View.GONE
        for (ob: ChannelAttachment in result.data!!.channels.attachments) {
            Log.d("onChannelListReceived", ob.objectType)

            val b = Button(this)
            b.text = String(Base64.decode(ob.displayName,Base64.NO_WRAP), Charset.defaultCharset())
            channelContainer.addView(b)
        }
    }
}