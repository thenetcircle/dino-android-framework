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

package com.thenetcircle.dinoandroidframework.dino.model.data

import android.util.Base64
import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 09/01/2018.
 */

class LoginModel(userID: Int, displayName: String, token: String) {

    @SerializedName("verb")
    private val verb = "login"
    @SerializedName("actor")
    private var actor: LoginActor = LoginActor(userID, displayName, token)

    private class LoginActor(userID: Int, displayName: String, token: String) {
        @SerializedName("id")
        val id: String = userID.toString() //<user ID>
        @SerializedName("displayName")
        val displayName: String = Base64.encodeToString(displayName.toByteArray(), Base64.NO_WRAP) //<user name>
        @SerializedName("attachments")
        val attachments: ArrayList<LoginAttachment> = ArrayList()

        init {
            attachments.add(LoginAttachment(token))
        }
    }

    private class LoginAttachment(token: String) {
        @SerializedName("objectType")
        val objectType: String = "token"
        @SerializedName("content")
        val content: String = token //<user token>
    }
}

