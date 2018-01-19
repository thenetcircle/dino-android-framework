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
import android.view.View
import android.widget.TextView
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dino.model.results.MessageReceived

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatViewHolderParent(v: View) : RecyclerView.ViewHolder(v) {
    private var chatView: View = v

    fun bind(message: MessageReceived) {
        val chatBox = chatView.findViewById<TextView>(R.id.chatMessage)
        chatBox.text = String(Base64.decode(message.objectX?.content, Base64.NO_WRAP))
    }
}