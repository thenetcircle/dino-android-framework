package com.thenetcircle.dinoandroidframework.dino.model.results

import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 15/01/2018.
 */

class RoomCreateResultModel : ModelResultParent() {
    @SerializedName("data")
    val data: RoomCreationData? = null
}

data class RoomCreationData(
        @SerializedName("target") val target: RoomCreationTarget,
        @SerializedName("object") val roomObject: RoomCreationObject,
        @SerializedName("verb") val verb: String //create
)

data class RoomCreationObject(
        @SerializedName("url") val url: String //<channel UUID>
)

data class RoomCreationTarget(
        @SerializedName("id") val id: String, //<the generated UUID for this room>
        @SerializedName("displayName") val displayName: String, //<name of the new room>
        @SerializedName("objectType") val objectType: String //temporary
)