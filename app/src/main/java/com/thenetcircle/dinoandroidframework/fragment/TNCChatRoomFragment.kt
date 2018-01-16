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

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomObject
import kotlinx.android.synthetic.main.fragment_chat_room.*

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomFragment : Fragment() , View.OnClickListener {

    interface ChatRoomListener {
        fun sendMessage(message: String)
    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var chatRoomListener : ChatRoomListener? = null
    var room : RoomObject? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_chat_room, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(activity)
        chatView.layoutManager = linearLayoutManager
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        chatRoomListener = context as ChatRoomListener
    }

    override fun onClick(v: View) {
        if(v == sendBtn) {
            if(!TextUtils.isEmpty(chatBox.text)) {
                chatRoomListener?.sendMessage(chatBox.text.toString())
                chatBox.text.clear()
            }
        }
    }
}