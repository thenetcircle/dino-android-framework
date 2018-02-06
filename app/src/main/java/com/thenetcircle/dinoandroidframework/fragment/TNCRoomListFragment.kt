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

package com.thenetcircle.dinoandroidframework.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.thenetcircle.dino.model.results.RequestRoomListResult
import com.thenetcircle.dino.model.results.RoomObject
import com.thenetcircle.dinoandroidframework.R
import kotlinx.android.synthetic.main.fragment_room_list.*
import java.nio.charset.Charset

/**
 * Created by aaron on 16/01/2018.
 */
class TNCRoomListFragment : Fragment(), View.OnClickListener {

    interface RoomListListener {
        fun createRoom(roomName: String, toUserID: Int)
        fun joinRoom(roomID: String)
    }

    var roomListInterface: RoomListListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_room_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_room.setOnClickListener {
            if (!TextUtils.isEmpty(room_name.text) && !TextUtils.isEmpty(user_id.text)) {
                roomListInterface?.createRoom(room_name.text.toString(), user_id.text.toString().toInt())
            }
        }
        loadingRooms()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        roomListInterface = context as RoomListListener
    }

    fun loadingRooms() {
        progress_wheel.visibility = View.VISIBLE
        room_list_container.removeAllViews()
    }

    fun listRooms(roomList: RequestRoomListResult) {
        room_list_container.removeAllViews()
        progress_wheel.visibility = View.GONE
        for (ob: RoomObject in roomList.data!!.objectX.attachments) {
            val b = Button(activity)
            b.text = String(Base64.decode(ob.displayName, Base64.NO_WRAP), Charset.defaultCharset())
            b.tag = ob.id
            b.setOnClickListener(this)
            room_list_container.addView(b)
        }
    }

    override fun onClick(v: View) {
        roomListInterface?.joinRoom(v.tag as String)
    }
}