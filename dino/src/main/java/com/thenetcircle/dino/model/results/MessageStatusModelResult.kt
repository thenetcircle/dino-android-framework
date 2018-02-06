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
import com.thenetcircle.dino.model.data.DeliveryReceiptModel


/**
 * Created by aaron on 01/02/2018.
 */
class MessageStatusModelResult : ModelResultParent() {
    @SerializedName("data")
    val data: MessageStatusModelData? = null
}

data class MessageStatusModelData(
        @SerializedName("object") val currentStatusObject: CurrentStatusObject,
        @SerializedName("verb") val verb: String, //check
        @SerializedName("id") val roomID: String, //<server-generated UUID>
        @SerializedName("published") val published: String //<server-generated timestamp RFC3339 format>
)

data class CurrentStatusObject(
        @SerializedName("attachments") val messageStatuses: List<CurrentStatus>
)

class CurrentStatus {
    @SerializedName("id")
    val id: String? = null //<msg UUID 1>
    @SerializedName("content")
    private val content: String? = null

    val status: DeliveryReceiptModel.DeliveryState
        get() = DeliveryReceiptModel.DeliveryState.getStatus(content!!)

}