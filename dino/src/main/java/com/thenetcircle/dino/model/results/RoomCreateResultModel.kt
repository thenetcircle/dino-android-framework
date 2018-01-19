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
 * Created by aaron on 15/01/2018.
 */

class RoomCreateResultModel : ModelResultParent() {
    @SerializedName("data")
    val data: RoomCreationData? = null
}

data class RoomCreationData(
        @SerializedName("target") val target: RoomCreationTarget,
        @SerializedName("object") val roomObject: RoomCreationObject,
        @SerializedName("verb") val verb: String //create
)

data class RoomCreationObject(
        @SerializedName("url") val url: String //<channel UUID>
)

data class RoomCreationTarget(
        @SerializedName("id") val id: String, //<the generated UUID for this room>
        @SerializedName("displayName") val displayName: String, //<name of the new room>
        @SerializedName("objectType") val objectType: String //temporary
)