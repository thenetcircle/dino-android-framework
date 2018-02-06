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
 * Request model for accessing read/received status of messages within a room
 *
 * @param userID the UUID of the receiving user
 */
class MessageStatusModel private constructor(userID: String) {
    @SerializedName("verb")
    private val verb: String = "check"
    @SerializedName("target")
    private val target: MessageStatusRoomTarget = MessageStatusRoomTarget(userID)
    @SerializedName("object")
    private var messageStatusRequestObject: MessageStatusRequestObject? = null

    /**
     * For checking a single Message
     *
     * @param roomID the UUID of the receiving user
     * @param message a MessageStatusRequest containing the message UUID (must be apart of the room requested)
     */
    constructor(userID: String, message: MessageStatusRequest) : this(userID) {
        val list = ArrayList<MessageStatusRequest>()
        list.add(message)
        messageStatusRequestObject = MessageStatusRequestObject(list)
    }

    /**
     * For checking a list of messages
     *
     * @param roomID the UUID of the room
     * @param messages a List of MessageStatusRequest objects containing the message UUID's to be checked (must be apart of the room requested)
     */
    constructor(roomID: String, messages: ArrayList<MessageStatusRequest>) : this(roomID) {
        messageStatusRequestObject = MessageStatusRequestObject(messages)
    }

    private class MessageStatusRequestObject(messages: List<MessageStatusRequest>) {
        @SerializedName("attachments")
        val attachments: List<MessageStatusRequest> = messages
    }

    /**
     * MessageStatusRequest Object
     *
     * @param messageID the UUID of a message
     */
    class MessageStatusRequest(messageID: String) {
        @SerializedName("id")
        val id: String = messageID
    }

    private class MessageStatusRoomTarget(roomID: String) {
        @SerializedName("id")
        val id: String = roomID
    }
}

