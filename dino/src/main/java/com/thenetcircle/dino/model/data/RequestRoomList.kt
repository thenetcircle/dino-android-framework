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
 * Request object for the accessible rooms within a channel
 *
 * @param channelID the uuid of then channel
 */
class RequestRoomList(channelID: String) {
    @SerializedName("verb")
    private var verb: String = "list"
    @SerializedName("object")
    private var roomObjectModel: RoomObjectModel = RoomObjectModel(channelID)

    private class RoomObjectModel(channelID: String) {
        @SerializedName("url")
        var channelID: String = channelID
    }
}