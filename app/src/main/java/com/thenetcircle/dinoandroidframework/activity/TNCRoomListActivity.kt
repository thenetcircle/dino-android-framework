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

package com.thenetcircle.dinoandroidframework.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.thenetcircle.dino.DinoError
import com.thenetcircle.dino.interfaces.DinoErrorListener
import com.thenetcircle.dino.interfaces.DinoRoomCreationListener
import com.thenetcircle.dino.interfaces.DinoRoomEntryListener
import com.thenetcircle.dino.model.data.CreateRoomPrivateModel
import com.thenetcircle.dino.model.data.RequestRoomList
import com.thenetcircle.dino.model.results.RoomCreateResultModel
import com.thenetcircle.dino.model.results.RequestRoomListResult
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

    override fun createRoom(roomName: String, toUserID: Int) {
        dinoChatConnection.createPrivateRoom(CreateRoomPrivateModel(intent.extras.getString(CHANNELID), roomName, loginObject!!.data!!.loginActor.id.toInt(), toUserID), object : DinoRoomCreationListener {
            override fun onResult(result: RoomCreateResultModel) {
                dinoChatConnection.getRoomList(RequestRoomList(intent.extras.getString(CHANNELID)), this@TNCRoomListActivity, this@TNCRoomListActivity)
            }
        }, this)
    }

    override fun onResume() {
        super.onResume()
        dinoChatConnection.getRoomList(RequestRoomList(intent.extras.getString(CHANNELID)), this, this)
    }

    override fun joinRoom(roomID: String) {
        val intent = Intent(this, TNCChatRoomActivity::class.java)
        intent.putExtra(TNCChatRoomActivity.ROOM_ID, roomID)
        startActivity(intent)
    }

    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResult(result: RequestRoomListResult) {
        roomListFragment.listRooms(result)
    }

}