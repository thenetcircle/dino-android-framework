package com.thenetcircle.dinoandroidsdk

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.thenetcircle.dinoandroidsdk.model.data.LoginModel
import com.thenetcircle.dinoandroidsdk.model.data.RoomListModel
import com.thenetcircle.dinoandroidsdk.model.results.ChannelAttachment
import com.thenetcircle.dinoandroidsdk.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidsdk.model.results.LoginModelResult
import com.thenetcircle.dinoandroidsdk.model.results.RoomListModelResult
import kotterknife.bindView

class MainActivity : AppCompatActivity(), DinoConnectionListener {

    var dinoChatConnection: DinoChatConnection = DinoChatConnection()
    val userDetailsView: View by bindView(R.id.user_details)
    val optionsView: View by bindView(R.id.connected_functions)
    val serverUrl: EditText by bindView(R.id.server_url)
    val userID: EditText by bindView(R.id.user_id)
    val displayName: EditText by bindView(R.id.display_name)
    val token: EditText by bindView(R.id.token)
    val statusBox: TextView by bindView(R.id.status_box);
    val connect: Button by bindView(R.id.connectBtn)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDetailsView.visibility = View.VISIBLE
        optionsView.visibility = View.GONE
        serverUrl.setText("http://10.60.1.124:9210/ws")
        userID.setText("179677")
        displayName.setText("Aaron")
        token.setText("54d088c7b78649dc3443a416afa0bd08ffcfa8a4")
        connect.setOnClickListener({
            val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

            if (!dinoChatConnection.isConnected) {
                statusBox.append("Connecting\n")
                dinoChatConnection.startConnection(serverUrl.text.toString(), this)
            } else {
                dinoChatConnection.disconnect(this)
            }
        })
    }

    override fun onConnect() {
        val loginModel = LoginModel(userID.text.toString().toInt(), displayName.text.toString(), token.text.toString())
        dinoChatConnection.login(loginModel, this)
        connect.text = getString(R.string.disconnect)
        runOnUiThread({
            statusBox.append("Connected\n")
            userDetailsView.visibility = View.GONE
            optionsView.visibility = View.VISIBLE
        })

    }

    override fun onDisconnect() {
        connect.text = getString(R.string.connect)
        runOnUiThread({
            statusBox.append("Disconnected\n")
            userDetailsView.visibility = View.VISIBLE
            optionsView.visibility = View.GONE
        })
    }

    override fun onError(error: DinoError) {
        runOnUiThread({ statusBox.append("Error: " + error.toString() + "\n") })
        dinoChatConnection.disconnect(this)
    }

    override fun onResult(loginModelResult: LoginModelResult) {
        runOnUiThread({ statusBox.append("Logged in Successfully\n") })
        //dinoChatConnection.getChannelList(ChannelListModel())
    }

    override fun onResult(channelListModelResult: ChannelListModelResult) {
        for (ob: ChannelAttachment in channelListModelResult.data!!.objectX.attachments) {
            Log.d("onChannelListReceived", ob.objectType)
            if (ob.objectType.toLowerCase() != "temporary") {
                dinoChatConnection.getRoomList(RoomListModel(ob.id), this)
            }
        }
    }

    override fun onResult(roomListModelResult: RoomListModelResult) {
        Log.e("onChannelRoomReceived", roomListModelResult.statusCode.toString())
    }
}
