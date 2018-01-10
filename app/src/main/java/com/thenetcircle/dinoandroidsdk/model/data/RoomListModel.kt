package com.thenetcircle.dinoandroidsdk.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by aaron on 09/01/2018.
 */
class RoomListModel(channelID: String) {
    @SerializedName("verb")
    private var verb : String = "list"
    @SerializedName("object")
    private var roomObjectModel : RoomObjectModel = RoomObjectModel(channelID)

    private class RoomObjectModel(channelID: String) {
        @SerializedName("url")
        var channelID : String = channelID
    }
}