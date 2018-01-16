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
 * Created by aaron on 16/01/2018.
 */

class ChatHistoryResult : ModelResultParent(){
		@SerializedName("data")
        val data: ChatHistoryData? = null
}

data class ChatHistoryData(
		@SerializedName("object") val chatHistory: ChatHistoryObject,
		@SerializedName("target") val target: ChatRoomTarget,
		@SerializedName("verb") val verb: String //history
)

data class ChatRoomTarget(
		@SerializedName("id") val id: String //<room UUID>
)

data class ChatHistoryObject(
		@SerializedName("objectType") val objectType: String, //messages
		@SerializedName("attachments") val attachments: List<ChatEntry>
)

data class ChatEntry(
		@SerializedName("author") val author: ChatHistoryAuthor,
		@SerializedName("id") val id: String, //<message ID>
		@SerializedName("content") val content: String, //<the message content>
		@SerializedName("published") val published: String //<the time it was sent RFC3339>
)

data class ChatHistoryAuthor(
		@SerializedName("id") val id: String, //<the user id of the sender>
		@SerializedName("displayName") val displayName: String //<the user name of the sender>
)