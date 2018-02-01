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

package com.thenetcircle.dino.model.data

import android.util.Base64
import com.google.gson.annotations.SerializedName


/**
 * Send a message to a room
 *
 * @param roomId the UUID of the room
 * @param message the message
 */
class ChatSendMessage(roomId: String, message: String) {
    @SerializedName("verb")
    private val verb: String = "send"
    @SerializedName("target")
    private val target: MessageRoomTarget = MessageRoomTarget(roomId)
    @SerializedName("object")
    private val messageContent: MessageContentObject = MessageContentObject(message)

    private class MessageContentObject(message: String) {
        @SerializedName("content")
        val content: String = Base64.encodeToString(message.toByteArray(), Base64.NO_WRAP)
    }

    private class MessageRoomTarget(id: String) {
        @SerializedName("id")
        val id: String = id
        @SerializedName("objectType")
        val objectType: String = "private"
    }

}