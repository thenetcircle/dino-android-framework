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
import java.io.Serializable


/**
 * Created by aaron on 15/01/2018.
 */

class JoinRoomResultModel : ModelResultParent() {
    @SerializedName("data")
    val data: JoinRoomData? = null
}

data class JoinRoomData(
        @SerializedName("object") val roomObjects: JoinRoomObjects,
        @SerializedName("verb") val verb: String, //join
        @SerializedName("target") val target: JoinRoomTarget
) : Serializable

data class JoinRoomTarget(
        @SerializedName("id") val id: String //<the room ID that the user joined>
) : Serializable

data class JoinRoomObjects(
        @SerializedName("objectType") val objectType: String, //room
        @SerializedName("attachments") val attachments: List<JoinRoomAttachment>
) : Serializable

data class JoinRoomAttachment(
        @SerializedName("objectType") val objectType: String, //history
        @SerializedName("attachments") val attachments: List<JoinRoomObjectAttachment>
) : Serializable

data class JoinRoomObjectAttachment(
        @SerializedName("author") val author: RoomObjectAuthor,
        @SerializedName("id") val id: String, //<message ID>
        @SerializedName("content") val content: String, //<the message content>
        @SerializedName("published") val published: String //<the time it was sent RFC3339>
) : Serializable

data class RoomObjectAuthor(
        @SerializedName("id") val id: String, //<the user id of the sender>
        @SerializedName("displayName") val displayName: String //<the user name of the sender>
) : Serializable