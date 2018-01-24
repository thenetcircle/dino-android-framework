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

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.thenetcircle.dino.DinoError
import com.thenetcircle.dino.interfaces.DinoChatMessageListener
import com.thenetcircle.dino.interfaces.DinoErrorListener
import com.thenetcircle.dino.interfaces.DinoJoinRoomListener
import com.thenetcircle.dino.interfaces.DinoMessageStatusUpdateListener
import com.thenetcircle.dino.model.data.ChatSendMessage
import com.thenetcircle.dino.model.data.DeliveryReceiptModel
import com.thenetcircle.dino.model.data.JoinRoomModel
import com.thenetcircle.dino.model.data.LeaveRoomModel
import com.thenetcircle.dino.model.results.*
import com.thenetcircle.dinoandroidframework.fragment.TNCChatRoomFragment

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomActivity : TNCBaseActivity(), DinoErrorListener, TNCChatRoomFragment.ChatRoomListener, DinoChatMessageListener {
    private val chatRoomFragment = TNCChatRoomFragment()

    companion object {
        const val ROOM_ID: String = "ROOM_ID"
    }

    private var roomID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTrans(chatRoomFragment)
        roomID = intent.extras.get(ROOM_ID) as String?
    }

    override fun onResume() {
        super.onResume()
        dinoChatConnection.joinRoom(JoinRoomModel(roomID!!), object : DinoJoinRoomListener {
            override fun onResult(result: JoinRoomResultModel) {
                chatRoomFragment.room = result
                result.data?.roomObjects?.attachments!!
                        .filter { it.objectType == "history" }
                        .forEach { processHistory(it) }

            }
        }, this)
        dinoChatConnection.registerMessageSentListener(this, this)
        dinoChatConnection.registerMessageStatusUpdateListener(object:DinoMessageStatusUpdateListener {
            override fun onResult(result: MessageStatus) {

            }
        }, this)
    }

    private fun processHistory(history: JoinRoomAttachment) {
        history.attachments
                .reversed()
                .forEach { chatRoomFragment.displayMessage(it) }
    }

    override fun onPause() {
        super.onPause()
        dinoChatConnection.unRegisterMessageSentListener()
        dinoChatConnection.leaveRoom(LeaveRoomModel(roomID!!), this)
    }

    override fun sendMessage(message: String) {
        dinoChatConnection.sendMessage(ChatSendMessage(roomID!!, message), this)
    }

    override fun onResult(result: MessageReceived) {
        //as we are on the chat screen, send the read
        val roomID = result.target?.id
        val delModel = DeliveryReceiptModel(DeliveryReceiptModel.DeliveryState.READ, roomID!!, DeliveryReceiptModel.DeliveryEntry(result.id!!))
        dinoChatConnection.sendMessageResponse(delModel, this)
        chatRoomFragment.displayMessage(result)
    }

    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResult(result: ChatSendMessageResult) {
        Log.d("TNCChatRoomActivity", "server received message")
    }
}