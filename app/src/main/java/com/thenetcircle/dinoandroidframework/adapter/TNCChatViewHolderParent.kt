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

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thenetcircle.dino.model.data.DeliveryReceiptModel
import com.thenetcircle.dinoandroidframework.R

/**
 * Created by aaron on 16/01/2018.
 */
open class TNCChatViewHolderParent(v: View) : RecyclerView.ViewHolder(v) {

    protected var chatView: View = v

    open fun bind(message: String, status: DeliveryReceiptModel.DeliveryState) {
        val chatBox = chatView.findViewById<TextView>(R.id.chatMessage)
        chatBox.text = message
    }
}

class TNCChatReceivedViewHolder(v: View) : TNCChatViewHolderParent(v) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.chat_room_received, parent, false)
        }
    }
}

class TNCChatSendViewHolder(v: View) : TNCChatViewHolderParent(v) {

    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.chat_room_sent, parent, false)
        }
    }

    override fun bind(message: String, status: DeliveryReceiptModel.DeliveryState) {
        super.bind(message, status)
        val statusView = chatView.findViewById<View>(R.id.messageStatus)
        if (status == DeliveryReceiptModel.DeliveryState.READ) {
            statusView.setBackgroundColor(Color.GREEN)
        } else {
            statusView.setBackgroundColor(Color.RED)
        }
    }
}
