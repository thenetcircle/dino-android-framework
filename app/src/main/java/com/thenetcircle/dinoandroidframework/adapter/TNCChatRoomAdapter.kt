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
import android.view.ViewGroup
import com.thenetcircle.dino.model.data.SendDeliveryReceiptModel
import com.thenetcircle.dino.model.results.JoinRoomObjectAttachment
import com.thenetcircle.dino.model.results.MessageReceived

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomAdapter(var myUserID: Int, var listener: TNCChatViewHolderParent.TNCChatViewClickListener) : RecyclerView.Adapter<TNCChatViewHolderParent>() {

    private class ChatMessage(var messageID: String, var content: String, var userID: Int, var state: SendDeliveryReceiptModel.DeliveryState)

    private var messages: ArrayList<ChatMessage> = ArrayList()

    fun addMessage(message: MessageReceived) {
        messages.add(0, ChatMessage(message.id!!, String(Base64.decode(message.objectX?.content, Base64.NO_WRAP)),
                message?.actor?.id!!.toInt(), SendDeliveryReceiptModel.DeliveryState.UNKNOWN))
        notifyDataSetChanged()
    }

    fun addMessage(message: JoinRoomObjectAttachment) {
        messages.add(0, ChatMessage(message.id!!, String(Base64.decode(message.content, Base64.NO_WRAP)),
                message.author?.id.toInt(), SendDeliveryReceiptModel.DeliveryState.UNKNOWN))
        notifyDataSetChanged()
    }

    fun updateMessageStatus(messageID: String, state: SendDeliveryReceiptModel.DeliveryState) {
        messages.filter { it.messageID == messageID }.forEach { it.state = state }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TNCChatViewHolderParent, position: Int) {
        if (holder is TNCChatSendViewHolder) {
            holder.bind(messages[position].messageID, messages[position].content, messages[position].state, listener)
        } else {
            holder.bind(messages[position].messageID, messages[position].content, messages[position].state, listener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TNCChatViewHolderParent {
        return if (viewType == 0) {
            TNCChatSendViewHolder(TNCChatSendViewHolder.createView(parent))
        } else {
            TNCChatReceivedViewHolder(TNCChatReceivedViewHolder.createView(parent))
        }
    }

    override fun getItemCount(): Int {
        return messages.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)
        if (message.userID == myUserID) {
            return 0
        }
        return 1
    }
}
