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
 * Created by aaron on 15/01/2018.
 */

class CreateRoomPrivateModel(channelURL: String, displayName: String, myUserID: Int, theirUserID: Int) {
    @SerializedName("verb")
    val verb: String = "create"
    @SerializedName("object")
    val objectData: RoomDataObject = RoomDataObject(channelURL)
    @SerializedName("target")
    val target: RoomTarget = RoomTarget(channelURL, displayName, myUserID, theirUserID)

    class RoomTarget(channelURL: String, displayName: String, myUserID: Int, theirUserID: Int) {
        @SerializedName("displayName")
        val displayName: String = Base64.encodeToString(displayName.toByteArray(), Base64.NO_WRAP)
        @SerializedName("objectType")
        val objectType: String = "private"
        @SerializedName("attatchments")
        val attatchments: ArrayList<RoomDataAttatchment> = ArrayList()

        init {
            val summary = myUserID.toString() + "," + theirUserID.toString()
            attatchments.add(RoomDataAttatchment(summary))
        }
    }

    class RoomDataObject(url: String) {
        @SerializedName("url")
        val url: String = url
    }

    class RoomDataAttatchment(summary: String) {
        @SerializedName("objectType")
        val objectType: String = "owners"

        @SerializedName("summary")
        val summary: String = summary
    }
}
