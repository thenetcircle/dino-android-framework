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
 * A Login in result model
 */
class LoginModelResult : ModelResultParent() {
    @SerializedName("data")
    val data: LoginData? = null
}

data class LoginData(
        @SerializedName("id") val id: String, //<server-generated UUID>
        @SerializedName("published") val published: String, //<server-generated timestamp, RFC3339 format>
        @SerializedName("actor") val loginActor: LoginActor,
        @SerializedName("object") val loginObject: LoginObject,
        @SerializedName("verb") val verb: String //login
)

data class LoginObject(
        @SerializedName("objectType") val objectType: String, //history
        @SerializedName("attachments") val attachments: List<LoginObjectAttachment>
)

data class LoginAttachment(
        @SerializedName("author") val author: LoginAuthor,
        @SerializedName("content") val content: String, //<message in base64>
        @SerializedName("id") val id: String, //84421980-d84a-4f6f-9ad7-0357d15d99f8
        @SerializedName("published") val published: String, //2017-11-17T07:19:12Z
        @SerializedName("summary") val summary: String, //9fa5b40a-f0a6-44ea-93c1-acf2947e5f09
        @SerializedName("objectType") val objectType: String //history
)

data class LoginAuthor(
        @SerializedName("id") val id: String, //<sender id>
        @SerializedName("displayName") val displayName: String //<sender name in base64>
)

data class LoginActor(
        @SerializedName("id") val id: String, //<user id>
        @SerializedName("displayName") val displayName: String, //<user name in base64>
        @SerializedName("attachments") val attachments: List<LoginAttachment>
)

data class LoginObjectAttachment(
        @SerializedName("objectType") val objectType: String, //room_role
        @SerializedName("id") val id: String, //<room UUID>
        @SerializedName("content") val content: String //moderatorowner
)
