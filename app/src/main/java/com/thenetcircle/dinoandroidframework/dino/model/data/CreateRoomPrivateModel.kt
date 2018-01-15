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
            val summary = myUserID.toString() + "," + theirUserID.toString();
            attatchments.add(RoomDataAttatchment(summary))
        }
    }

    class RoomDataObject(url : String) {
        @SerializedName("url")
        val url: String = url
    }

    class RoomDataAttatchment( summary: String) {
        @SerializedName("objectType")
        val objectType: String = "owners"

        @SerializedName("summary")
        val summary: String = summary
    }
}
