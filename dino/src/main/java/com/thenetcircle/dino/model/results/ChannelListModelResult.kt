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

package com.thenetcircle.dino.model.results

import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 09/01/2018.
 */

class ChannelListModelResult : ModelResultParent() {
    @SerializedName("data")
    val data: ChannelListData? = null
}

data class ChannelListData(
        @SerializedName("object") val channels: ChannelListObject,
        @SerializedName("verb") val verb: String //list
)

data class ChannelListObject(
        @SerializedName("objectType") val objectType: String, //channels
        @SerializedName("attachments") val attachments: List<ChannelAttachment>
)

data class ChannelAttachment(
        @SerializedName("id") val id: String, //<channel UUID>
        @SerializedName("displayName") val displayName: String, //<channel name>
        @SerializedName("url") val url: Int, //8
        @SerializedName("objectType") val objectType: String, //static
        @SerializedName("attachments") val attachments: List<Attachment>
)

data class Attachment(
        @SerializedName("summary") val summary: String, //message
        @SerializedName("objectType") val objectType: String, //membership
        @SerializedName("content") val content: String //1
)
