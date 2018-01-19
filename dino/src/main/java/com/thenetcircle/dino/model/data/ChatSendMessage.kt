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
 * Created by aaron on 16/01/2018.
 */


class ChatSendMessage(roomId: String, message: String) {
    @SerializedName("verb")
    val verb: String = "send"
    @SerializedName("target")
    val target: MessageRoomTarget = MessageRoomTarget(roomId)
    @SerializedName("object")
    val messageContent: MessageContentObject = MessageContentObject(message)

    class MessageContentObject(message: String) {
        @SerializedName("content")
        val content: String = Base64.encodeToString(message.toByteArray(), Base64.NO_WRAP)
    }

    class MessageRoomTarget(id: String) {
        @SerializedName("id")
        val id: String = id
        @SerializedName("objectType")
        val objectType: String = "private"
    }

}