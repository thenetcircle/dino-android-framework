package com.thenetcircle.dinoandroidframework.fragment

import android.app.Fragment
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomListModelResult
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomObject
import kotlinx.android.synthetic.main.fragment_room_list.*
import java.nio.charset.Charset

/**
 * Created by aaron on 16/01/2018.
 */
class TNCRoomListFragment : Fragment() {

    interface RoomListListener {
        fun createRoom(roomName: String)
        fun joinRoom(roomID: String)
    }

    var roomListInterface: RoomListListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_room_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_room.setOnClickListener {
            if (!TextUtils.isEmpty(room_name.text.toString())) {
                roomListInterface?.createRoom(room_name.text.toString())
            }
        }
        loadingRooms()
    }

    fun loadingRooms() {
        progress_wheel.visibility = View.VISIBLE
        room_list_container.removeAllViews();

    }

    fun listRooms(roomList: RoomListModelResult) {
        progress_wheel.visibility = View.GONE
        for (ob: RoomObject in roomList.data!!.objectX.attachments) {
            val b = Button(activity)
            b.text = String(Base64.decode(ob.displayName, Base64.NO_WRAP), Charset.defaultCharset())
            b.tag = ob.id
            b.setOnClickListener {
                roomListInterface?.joinRoom(b.tag as String)
            }
            room_list_container.addView(b)
        }
    }


}