package com.thenetcircle.dinoandroidsdk.model.data
import com.google.gson.annotations.SerializedName
import android.util.Base64


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
		val displayName: String =  Base64.encodeToString(displayName.toByteArray(), Base64.NO_WRAP) //<user name>
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

