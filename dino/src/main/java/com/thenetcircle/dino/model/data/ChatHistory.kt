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
 * ChatHistory
 *
 * A Model for obtaining the Chat history of a given room
 * @param roomID the UUID of the room
 * @param updated the time stamp from which the history should start
 */
class ChatHistory(roomID: String, updated: String) {

    @SerializedName("target")
    private val target: ChatHistoryTarget = ChatHistoryTarget(roomID)
    @SerializedName("updated")
    private val updated: String = updated //<last read timestamp, if configured in server will return messages since this time>
    @SerializedName("verb")
    private val verb: String = "list"

    private class ChatHistoryTarget(id: String) {
        @SerializedName("id")
        val id: String = id //<room UUID>
    }
}
