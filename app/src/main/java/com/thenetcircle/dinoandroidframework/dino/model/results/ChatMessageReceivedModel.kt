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

package com.thenetcircle.dinoandroidframework.dino.model.results

import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 17/01/2018.
 */


class ChatSendMessageResult() : ModelResultParent() {
    @SerializedName("data")
    val data: ChatSendMessageData? = null
}

data class ChatSendMessageData(
        @SerializedName("id") val id: String, //c42ebf01-3d50-4f27-a345-4ed213be045d
        @SerializedName("published") val published: String, //2016-10-07T10:45:34Z
        @SerializedName("actor") val actor: MessageUser,
        @SerializedName("verb") val verb: String, //send
        @SerializedName("target") val target: MessageRoomTarget,
        @SerializedName("object") val message: ChatMessageObject
)

data class MessageRoomTarget(
        @SerializedName("id") val id: String, //<room ID>
        @SerializedName("displayName") val displayName: String //<room name>
)

data class ChatMessageObject(
        @SerializedName("content") val content: String, //<the message>
        @SerializedName("displayName") val displayName: String, //<the channel name>
        @SerializedName("url") val url: String, //<the channel id>
        @SerializedName("objectType") val objectType: String //<room/private>
)

data class MessageUser(
        @SerializedName("id") val id: String, //<your user ID>
        @SerializedName("displayName") val displayName: String //<your user name>
)