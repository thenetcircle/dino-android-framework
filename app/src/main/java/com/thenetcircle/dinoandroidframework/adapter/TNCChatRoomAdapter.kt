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

package com.thenetcircle.dinoandroidframework.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dinoandroidframework.dino.model.results.MessageReceived

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomAdapter(myUserID: Int) : RecyclerView.Adapter<TNCChatViewHolderParent>() {

    var messages: ArrayList<MessageReceived> = ArrayList()
    var myID: Int = myUserID

    fun addMessage(message: MessageReceived) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TNCChatViewHolderParent, position: Int) {
        holder.bind(messages[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TNCChatViewHolderParent {
        val inflatedView = LayoutInflater.from(parent.context).inflate(if (viewType == 0) R.layout.chat_room_sent else R.layout.chat_room_received, parent, false)
        return TNCChatViewHolderParent(inflatedView)
    }

    override fun getItemCount(): Int {
        return messages.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)
        if (message?.actor?.id?.toInt() == myID) {
            return 0
        }
        return 1
    }
}
