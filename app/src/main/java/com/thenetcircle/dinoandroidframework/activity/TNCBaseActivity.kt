/*
 * Copyright 2018 The NetCircle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thenetcircle.dinoandroidframework.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Toast
import com.thenetcircle.dino.DinoChatConnection
import com.thenetcircle.dino.DinoError
import com.thenetcircle.dino.interfaces.DinoConnectionListener
import com.thenetcircle.dino.interfaces.DinoErrorListener
import com.thenetcircle.dino.interfaces.DinoMessageReceivedListener
import com.thenetcircle.dino.model.results.LoginModelResult
import com.thenetcircle.dino.model.results.MessageReceived
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dinoandroidframework.TNCApplication

/**
 * Created by aaron on 11/01/2018.
 */
open class TNCBaseActivity : AppCompatActivity(), DinoConnectionListener, DinoErrorListener, DinoMessageReceivedListener {

    companion object {
        var loginObject: LoginModelResult? = null
    }

    var dinoChatConnection: DinoChatConnection = TNCApplication.dinoConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dinoChatConnection.connectionListener = this
        dinoChatConnection.messageReceivedListener = this
    }

    override fun onResume() {
        super.onResume()
        if (dinoChatConnection.isLoggedIn) {
            dinoChatConnection.registerMessageListener(this)
        }
    }

    override fun onPause() {
        super.onPause()
        dinoChatConnection.unRegisterMessageListener()
    }

    protected fun fragmentTrans(frag: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, frag)
                .commit()
    }

    fun isDinoConnected(): Boolean {
        return dinoChatConnection.isConnected
    }

    override fun onConnect() {
    }

    override fun onDisconnect() {
        Toast.makeText(this, "You have been Disconnected", Toast.LENGTH_LONG).show()
    }

    override fun onResult(result: MessageReceived) {
        val text = String(Base64.decode(result.objectX?.content, Base64.NO_WRAP))
        val senderID = result.actor?.id
        Toast.makeText(this, "Message Received: $text From User: $senderID", Toast.LENGTH_LONG).show()
    }


    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }
}