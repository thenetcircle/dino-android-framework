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
 * Join a room request model
 *
 * @param roomID the UUID of a room
 */
class JoinRoomModel(roomID: String) {

    @SerializedName("verb")
    private val verb: String = "join"
    @SerializedName("target")
    private val target: JoinRoomTarget = JoinRoomTarget(roomID)

    private class JoinRoomTarget(roomID: String) {
        @SerializedName("id")
        val id: String = roomID
    }
}

