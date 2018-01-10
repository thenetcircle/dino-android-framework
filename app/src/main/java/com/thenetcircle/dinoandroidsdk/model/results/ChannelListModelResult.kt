package com.thenetcircle.dinoandroidsdk.model.results
import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 09/01/2018.
 */

data class ChannelListModelResult(
		@SerializedName("status_code") val statusCode: Int, //200
		@SerializedName("data") val data: ChannelListData
)

data class ChannelListData(
		@SerializedName("object") val objectX: ChannelListObject,
		@SerializedName("verb") val verb: String //list
)

data class ChannelListObject(
		@SerializedName("objectType") val objectType: String, //channels
		@SerializedName("attachments") val attachments: List<ChannelAttachment>
)

data class ChannelAttachment(
		@SerializedName("id") val id: String, //<channel UUID>
		@SerializedName("displayName") val displayName: String, //<channel name>
		@SerializedName("url") val url: Int, //8
		@SerializedName("objectType") val objectType: String, //static
		@SerializedName("attachments") val attachments: List<Attachment>
)

data class Attachment(
		@SerializedName("summary") val summary: String, //message
		@SerializedName("objectType") val objectType: String, //membership
		@SerializedName("content") val content: String //1
)
