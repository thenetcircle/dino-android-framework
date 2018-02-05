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
import com.thenetcircle.dino.interfaces.*
import com.thenetcircle.dino.model.data.*
import com.thenetcircle.dino.model.results.*
import com.thenetcircle.dinoandroidframework.fragment.TNCChatRoomFragment

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomActivity : TNCBaseActivity(), DinoErrorListener, TNCChatRoomFragment.ChatRoomListener, DinoChatMessageListener {
    companion object {
        const val ROOM_ID: String = "ROOM_ID"
    }

    private class TNCChatRoomUser(val userID: String)

    private val chatRoomFragment = TNCChatRoomFragment()
    private var roomID: String? = ""
    private val roomUserList: ArrayList<TNCChatRoomUser> = ArrayList()

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

                result.data?.roomObjects?.attachments!!
                        .filter { it.objectType == "owner" }
                        .forEach {
                            it.attachments.forEach {
                                roomUserList.add(TNCChatRoomUser(it.id))
                            }
                        }
            }
        }, this)
        dinoChatConnection.registerMessageSentListener(this, this)
        dinoChatConnection.registerMessageStatusUpdateListener(object : DinoMessageStatusUpdateListener {
            override fun onResult(result: MessageStatus) {
                chatRoomFragment.updateMessageStatus(result)
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
        dinoChatConnection.unRegisterMessageStatusUpdateListener()
    }

    override fun sendMessage(message: String) {
        dinoChatConnection.sendMessage(ChatSendMessage(roomID!!, message), this)
    }

    override fun requestMessageStatus(messageId: String) {

        roomUserList
                .filter { it.userID != loginObject?.data?.loginActor?.id }
                .forEach {
                    dinoChatConnection.getStatusHistory(MessageStatusModel(it.userID, MessageStatusModel.MessageStatusRequest(messageId)),
                            object : DinoMessageStatusRequestListener {
                                override fun onResult(result: MessageStatusModelResult) {
                                    Log.d("status  update", result.toString())
                                }
                            }, this)
                }
    }

    override fun onResult(result: MessageReceived) {
        //as we are on the chat screen, send the read
        if (result.actor?.id != loginObject?.data?.loginActor?.id) {
            val roomID = result.target?.id
            val delModel = DeliveryReceiptModel(DeliveryReceiptModel.DeliveryState.READ, roomID!!, DeliveryReceiptModel.DeliveryEntry(result.id!!))
            dinoChatConnection.sendMessageResponseRead(delModel, this)
        }
        chatRoomFragment.displayMessage(result)
    }

    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResult(result: ChatSendMessageResult) {
        Log.d("TNCChatRoomActivity", "server received message")
    }
}