package com.thenetcircle.dinoandroidsdk.model.results

import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 09/01/2018.
 */


class RoomListModelResult : ModelResultParent() {
    @SerializedName("data")
    val data: RoomData? = null
}

data class RoomData(
        @SerializedName("object") val objectX: RoomListObject,
        @SerializedName("verb") val verb: String //list
)

data class RoomListObject(
        @SerializedName("objectType") val objectType: String, //rooms
        @SerializedName("url") val url: String, //<channel UUID>
        @SerializedName("attachments") val attachments: List<RoomObject>
)

data class RoomObject(
        @SerializedName("id") val id: String, //<room UUID>
        @SerializedName("displayName") val displayName: String, //<room name>
        @SerializedName("url") val url: Int, //8
        @SerializedName("summary") val summary: Int, //1
        @SerializedName("objectType") val objectType: String, //static
        @SerializedName("content") val content: String, //moderator,owner
        @SerializedName("attachments") val attachments: List<RoomObjectAttachment>
)

data class RoomObjectAttachment(
        @SerializedName("summary") val summary: String, //join
        @SerializedName("objectType") val objectType: String, //gender
        @SerializedName("content") val content: String //f
)