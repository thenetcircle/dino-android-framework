package com.thenetcircle.dinoandroidframework

import android.os.Bundle
import android.widget.Toast
import com.thenetcircle.dinoandroidframework.dino.DinoError
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoJoinRoomListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoRoomCreationListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoRoomEntryListener
import com.thenetcircle.dinoandroidframework.dino.model.data.CreateRoomPrivateModel
import com.thenetcircle.dinoandroidframework.dino.model.data.JoinRoomModel
import com.thenetcircle.dinoandroidframework.dino.model.data.RoomListModel
import com.thenetcircle.dinoandroidframework.dino.model.results.JoinRoomResultModel
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomCreateResultModel
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomListModelResult
import com.thenetcircle.dinoandroidframework.fragment.TNCRoomListFragment

/**
 * Created by aaron on 15/01/2018.
 */
class TNCRoomListActivity : TNCBaseActivity(), DinoRoomEntryListener, DinoErrorListener, TNCRoomListFragment.RoomListListener {

    companion object {
        val CHANNELID: String = "CHANNEL_ID"
    }

    private val roomListFragment: TNCRoomListFragment = TNCRoomListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTrans(roomListFragment)
    }

    override fun createRoom(roomName: String) {
        dinoChatConnection.createPrivateRoom(CreateRoomPrivateModel(intent.extras.getString(CHANNELID), roomName, loginObject!!.data!!.actor.id.toInt(), 1234), object : DinoRoomCreationListener {
            override fun onResult(result: RoomCreateResultModel) {
                dinoChatConnection.getRoomList(RoomListModel(intent.extras.getString(CHANNELID)), this@TNCRoomListActivity, this@TNCRoomListActivity)
            }
        }, this)
    }

    override fun onResume() {
        super.onResume()
        dinoChatConnection.getRoomList(RoomListModel(intent.extras.getString(CHANNELID)), this, this)
    }

    override fun joinRoom(roomID: String) {
        dinoChatConnection.joinRoom(JoinRoomModel(roomID), object : DinoJoinRoomListener {
            override fun onResult(result: JoinRoomResultModel) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }, this)
    }

    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResult(result: RoomListModelResult) {
        roomListFragment.listRooms(result)
    }

}