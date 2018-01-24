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
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thenetcircle.dino.model.data.DeliveryReceiptModel
import com.thenetcircle.dino.model.results.JoinRoomObjectAttachment
import com.thenetcircle.dino.model.results.MessageReceived
import com.thenetcircle.dinoandroidframework.R

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomAdapter(myUserID: Int) : RecyclerView.Adapter<TNCChatViewHolderParent>() {


    class CHatMessage(var messageID: String, var content: String, var userID: Int, var state: DeliveryReceiptModel.DeliveryState)


    var messages: ArrayList<CHatMessage> = ArrayList()
    var myID: Int = myUserID

    fun addMessage(message: MessageReceived) {
        messages.add(0, CHatMessage(message.id!!, String(Base64.decode(message.objectX?.content, Base64.NO_WRAP)),
                message?.actor?.id!!.toInt(), DeliveryReceiptModel.DeliveryState.UNKNOWN))
        notifyDataSetChanged()
    }

    fun addMessage(message: JoinRoomObjectAttachment) {
        messages.add(0, CHatMessage(message.id!!, String(Base64.decode(message.content, Base64.NO_WRAP)),
                message.author?.id.toInt(), DeliveryReceiptModel.DeliveryState.UNKNOWN))
        notifyDataSetChanged()
    }

    fun updateMessageStatus(messageID: String, state: DeliveryReceiptModel.DeliveryState) {
        messages.filter { it.messageID == messageID }.forEach { it.state = state }
    }

    override fun onBindViewHolder(holder: TNCChatViewHolderParent, position: Int) {
        holder.bind(messages[position].content)
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
        if (message.userID == myID) {
            return 0
        }
        return 1
    }
}
