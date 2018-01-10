package com.thenetcircle.dinoandroidsdk.model.results
import com.google.gson.annotations.SerializedName


/**
 * Created by aaron on 09/01/2018.
 */


class LoginModelResult : ModelResultParent() {
	@SerializedName("data")
	val data: Data? = null
}

data class Data(
        @SerializedName("id") val id: String, //<server-generated UUID>
        @SerializedName("published") val published: String, //<server-generated timestamp, RFC3339 format>
        @SerializedName("actor") val actor: LoginResultActor,
        @SerializedName("object") val objectX: Object,
        @SerializedName("verb") val verb: String //login
)

data class Object(
		@SerializedName("objectType") val objectType: String, //history
		@SerializedName("attachments") val attachments: List<ObjectAttachment>
)

data class ActorAttachment(
        @SerializedName("author") val author: Author,
        @SerializedName("content") val content: String, //<message in base64>
        @SerializedName("id") val id: String, //84421980-d84a-4f6f-9ad7-0357d15d99f8
        @SerializedName("published") val published: String, //2017-11-17T07:19:12Z
        @SerializedName("summary") val summary: String, //9fa5b40a-f0a6-44ea-93c1-acf2947e5f09
        @SerializedName("objectType") val objectType: String //history
)

data class Author(
		@SerializedName("id") val id: String, //<sender id>
		@SerializedName("displayName") val displayName: String //<sender name in base64>
)

data class LoginResultActor(
		@SerializedName("id") val id: String, //<user id>
		@SerializedName("displayName") val displayName: String, //<user name in base64>
		@SerializedName("attachments") val attachments: List<ActorAttachment>
)

data class ObjectAttachment(
		@SerializedName("objectType") val objectType: String, //room_role
		@SerializedName("id") val id: String, //<room UUID>
		@SerializedName("content") val content: String //moderatorowner
)
