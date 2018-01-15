package com.thenetcircle.dinoandroidframework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.thenetcircle.dinoandroidframework.dino.DinoError
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoConnectionListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoLoginListener
import com.thenetcircle.dinoandroidframework.dino.model.data.LoginModel
import com.thenetcircle.dinoandroidframework.dino.model.results.LoginModelResult
import kotterknife.bindView

class TNCMainActivity : TNCBaseActivity(), DinoConnectionListener, DinoLoginListener, DinoErrorListener {

    val userDetailsView: View by bindView(R.id.user_details)
    val optionsView: View by bindView(R.id.connected_functions)
    val serverUrl: EditText by bindView(R.id.server_url)
    val userID: EditText by bindView(R.id.user_id)
    val displayName: EditText by bindView(R.id.display_name)
    val token: EditText by bindView(R.id.token)
    val statusBox: TextView by bindView(R.id.status_box)
    val connect: Button by bindView(R.id.connectBtn)
    val channel: Button by bindView(R.id.channel_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDetailsView.visibility = View.VISIBLE
        optionsView.visibility = View.GONE
        serverUrl.setText("http://10.60.1.124:9210/ws")
        userID.setText("179677")
        displayName.setText("Aaron")
        token.setText("b5335f65774696911e9fba71fe9f3430ce7f801c")
        connect.setOnClickListener({
            if (currentFocus.windowToken != null) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }

            if (!dinoChatConnection.isConnected) {
                statusBox.append("Connecting\n")
                dinoChatConnection.startConnection(serverUrl.text.toString(), this, this)
            } else {
                dinoChatConnection.disconnect()
                statusBox.append("Disconnected\n")
                userDetailsView.visibility = View.VISIBLE
                optionsView.visibility = View.GONE
            }
        })

        channel.setOnClickListener({
            val intent = Intent(this, TNCChannelList::class.java)
            startActivity(intent)
        })
    }

    override fun onConnect() {
        val loginModel = LoginModel(userID.text.toString().toInt(), displayName.text.toString(), token.text.toString())
        dinoChatConnection.login(loginModel, this , this)
        connect.text = getString(R.string.disconnect)
        runOnUiThread({
            statusBox.append("Connected\n")
            userDetailsView.visibility = View.GONE
            optionsView.visibility = View.VISIBLE
        })

    }

    override fun onError(error: DinoError) {
        statusBox.append("Error: " + error.toString() + "\n")
        connect.text = getString(R.string.connect)
        userDetailsView.visibility = View.VISIBLE
        optionsView.visibility = View.GONE
        dinoChatConnection.disconnect()

    }

    override fun onResult(loginModelResult: LoginModelResult) {
        statusBox.append("Logged in Successfully\n")
        //dinoChatConnection.getChannelList(ChannelListModel())
    }
}
