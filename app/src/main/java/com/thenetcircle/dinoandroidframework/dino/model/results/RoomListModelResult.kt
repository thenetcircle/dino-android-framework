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
 * Created by aaron on 09/01/2018.
 */


class RoomListModelResult : ModelResultParent() {
    @SerializedName("data")
    val data: RoomData? = null
}

data class RoomData(
        @SerializedName("object") val objectX: RoomListObject,
        @SerializedName("verb") val verb: String //list
)

data class RoomListObject(
        @SerializedName("objectType") val objectType: String, //rooms
        @SerializedName("url") val url: String, //<channel UUID>
        @SerializedName("attachments") val attachments: List<RoomObject>
)

data class RoomObject(
        @SerializedName("id") val id: String, //<room UUID>
        @SerializedName("displayName") val displayName: String, //<room name>
        @SerializedName("url") val url: Int, //8
        @SerializedName("summary") val summary: Int, //1
        @SerializedName("objectType") val objectType: String, //static
        @SerializedName("content") val content: String, //moderator,owner
        @SerializedName("attachments") val attachments: List<RoomObjectAttachment>
) : Serializable

data class RoomObjectAttachment(
        @SerializedName("summary") val summary: String, //join
        @SerializedName("objectType") val objectType: String, //gender
        @SerializedName("content") val content: String //f
)