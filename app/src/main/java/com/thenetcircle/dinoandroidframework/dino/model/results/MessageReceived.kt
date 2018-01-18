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
 * Created by aaron on 18/01/2018.
 */
class MessageReceived : ModelResultParent() {
    @SerializedName("object")
    val objectX: MessageReceivedObject? = null
    @SerializedName("target")
    val target: MessageReceivedTarget? = null
    @SerializedName("verb")
    val verb: String = "send"
    @SerializedName("actor")
    val actor: MessageReceivedActor? = null
    @SerializedName("published")
    val published: String? = null //2018-01-18T05:34:56Z
    @SerializedName("id")
    val id: String? = null //32e56aa2-5bc4-40aa-be41-a20a2277e8b3
}

data class MessageReceivedActor(
        @SerializedName("id") val id: String, //179677
        @SerializedName("displayName") val displayName: String //QWFyb24=
)

data class MessageReceivedTarget(
        @SerializedName("id") val id: String, //6c84c29f-465b-46ee-be7a-490042e4c38e
        @SerializedName("objectType") val objectType: String, //private
        @SerializedName("displayName") val displayName: String
)

data class MessageReceivedObject(
        @SerializedName("content") val content: String, //aGVsbG8=
        @SerializedName("url") val url: String, //6cba7e00-6b0e-4bee-ad59-98bf90813fd0
        @SerializedName("displayName") val displayName: String
)