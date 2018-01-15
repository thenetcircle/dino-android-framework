package com.thenetcircle.dinoandroidframework.dino.model.data

import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 15/01/2018.
 */
class JoinRoomModel(roomID: String) {

    @SerializedName("verb")
    val verb: String = "join"
    @SerializedName("target")
    val target: JoinRoomTarget = JoinRoomTarget(roomID)

    class JoinRoomTarget(roomID: String) {
        @SerializedName("id")
        val id: String = roomID
    }
}

