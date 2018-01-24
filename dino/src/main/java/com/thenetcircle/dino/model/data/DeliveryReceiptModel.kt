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

import com.google.gson.annotations.SerializedName

/**
 * Created by aaron on 18/01/2018.
 */

class DeliveryReceiptModel private constructor(deliveryState: DeliveryState, roomID: String) {

    enum class DeliveryState(val state: String) {
        UNKNOWN("unknown"),
        RECEIVED("receive"),
        READ("read")
    }

    @SerializedName("verb")
    val verb: String = deliveryState.state
    @SerializedName("target")
    val target: DeliveryTarget = DeliveryTarget(roomID)
    @SerializedName("object")
    var deliveryObject: DeliveryObject? = null

    constructor(deliveryState: DeliveryState, roomID: String, message: DeliveryEntry) : this(deliveryState, roomID) {
        val list = ArrayList<DeliveryEntry>()
        list.add(message)
        deliveryObject = DeliveryObject(list)
    }

    constructor(deliveryState: DeliveryState, roomID: String, messages: List<DeliveryEntry>) : this(deliveryState, roomID) {
        deliveryObject = DeliveryObject(messages)
    }


    class DeliveryObject(messageIDs: List<DeliveryEntry>) {
        @SerializedName("attachments")
        val attachments: List<DeliveryEntry> = messageIDs
    }

    class DeliveryEntry(messageUUID: String) {
        @SerializedName("id")
        val id: String = messageUUID
    }

    class DeliveryTarget(roomID: String) {
        @SerializedName("id")
        val id: String = roomID//<uuid of the room the messages are all in>
    }

}
