package com.thenetcircle.dinoandroidframework.dino.model.results

import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 15/01/2018.
 */

class JoinRoomResultModel : ModelResultParent() {
    @SerializedName("data")
    val data: JoinRoomData? = null
}

data class JoinRoomData(
        @SerializedName("object") val roomObjects: JoinRoomObjects,
        @SerializedName("verb") val verb: String, //join
        @SerializedName("target") val target: JoinRoomTarget
)

data class JoinRoomTarget(
        @SerializedName("id") val id: String //<the room ID that the user joined>
)

data class JoinRoomObjects(
        @SerializedName("objectType") val objectType: String, //room
        @SerializedName("attachments") val attachments: List<JoinRoomAttachment>
)

data class JoinRoomAttachment(
        @SerializedName("objectType") val objectType: String, //history
        @SerializedName("attachments") val attachments: List<JoinRoomObjectAttachment>
)

data class JoinRoomObjectAttachment(
        @SerializedName("author") val author: RoomObjectAuthor,
        @SerializedName("id") val id: String, //<message ID>
        @SerializedName("content") val content: String, //<the message content>
        @SerializedName("published") val published: String //<the time it was sent RFC3339>
)

data class RoomObjectAuthor(
        @SerializedName("id") val id: String, //<the user id of the sender>
        @SerializedName("displayName") val displayName: String //<the user name of the sender>
)