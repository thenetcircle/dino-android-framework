package com.thenetcircle.dinoandroidframework

import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.thenetcircle.dinoandroidframework.dino.DinoError
import com.thenetcircle.dinoandroidframework.dino.interfaces.*
import com.thenetcircle.dinoandroidframework.dino.model.data.CreateRoomPrivateModel
import com.thenetcircle.dinoandroidframework.dino.model.data.JoinRoomModel
import com.thenetcircle.dinoandroidframework.dino.model.data.RoomListModel
import com.thenetcircle.dinoandroidframework.dino.model.results.*
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
    private val createRoomEditBox : EditText by bindView(R.id.room_name)
    private val createRoomButton : Button by bindView(R.id.create_room)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)

        createRoomButton.setOnClickListener {
            if(!TextUtils.isEmpty(createRoomEditBox.text.toString())) {
                dinoChatConnection.createPrivateRoom(CreateRoomPrivateModel(intent.extras.getString(CHANNELID), createRoomEditBox.text.toString(), loginObject!!.data!!.actor.id.toInt(),1234), object: DinoRoomCreationListener {
                    override fun onResult(result: RoomCreateResultModel) {
                        progressBar.visibility = View.VISIBLE
                        roomContainer.removeAllViews()
                        dinoChatConnection.getRoomList(RoomListModel(intent.extras.getString(CHANNELID)), this@TNCRoomList, this@TNCRoomList)
                    }
                }, this)
            }
        }
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
            b.tag = ob.id
            b.setOnClickListener {
                dinoChatConnection.joinRoom(JoinRoomModel(b.tag as String), object: DinoJoinRoomListener
                {
                    override fun onResult(result: JoinRoomResultModel) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                }, this)
            }
            roomContainer.addView(b)
        }
    }

}