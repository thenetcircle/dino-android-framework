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
 * Created by aaron on 23/01/2018.
 */
class MessageStatus : ModelResultParent() {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("published")
    val published: String? = null
    @SerializedName("actor")
    val receivingUser: ReceivingUser? = null
    @SerializedName("verb")
    val verb: String? = null
    @SerializedName("target")
    val target: ReceivingTarget? = null
    @SerializedName("object")
    val messageObject: ReceivedMessages? = null
}

data class ReceivedMessages(
        @SerializedName("attachments") val attachments: List<ReceivedMessage>
)

data class ReceivedMessage(
        @SerializedName("id") val id: String //<message1 uuid>
)

data class ReceivingUser(
        @SerializedName("id") val id: String //<user id of the one sending the read receipt>
)

data class ReceivingTarget(
        @SerializedName("id") val id: String //<uuid of the room the messages are all in>
)