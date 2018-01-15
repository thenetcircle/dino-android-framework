package com.thenetcircle.dinoandroidframework

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.thenetcircle.dinoandroidframework.dino.DinoError
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoRoomEntryListener
import com.thenetcircle.dinoandroidframework.dino.model.data.RoomListModel
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomListModelResult
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomObject
import kotterknife.bindView
import java.nio.charset.Charset

/**
 * Created by aaron on 15/01/2018.
 */
class TNCRoomList : TNCBaseActivity(), DinoRoomEntryListener, DinoErrorListener {

    companion object {
        val CHANNELID: String = "CHANNEL_ID"
    }

    private val roomContainer : LinearLayout by bindView(R.id.room_list_container)
    private val progressBar: ProgressBar by bindView(R.id.progress_wheel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE
        dinoChatConnection.getRoomList(RoomListModel(intent.extras.getString(CHANNELID)), this, this)

    }

    override fun onError(error: DinoError) {
        progressBar.visibility = View.GONE
    }

    override fun onResult(result: RoomListModelResult) {
        progressBar.visibility = View.GONE
        for (ob: RoomObject in result.data!!.objectX.attachments) {
            val b = Button(this)
            b.text = String(Base64.decode(ob.displayName, Base64.NO_WRAP), Charset.defaultCharset())
            roomContainer.addView(b)
        }
    }
}