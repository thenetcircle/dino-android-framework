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

package com.thenetcircle.dinoandroidframework.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.thenetcircle.dino.DinoError
import com.thenetcircle.dino.model.data.LoginModel
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dinoandroidframework.activity.TNCBaseActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by aaron on 16/01/2018.
 */

class TNCLoginFragment : Fragment() {

    interface LoginFragmentListener {
        fun onConnectToService(url: String)
        fun onDisconnectFromService()
        fun onLoginToService(loginModel: LoginModel)
        fun goToChannelList()
    }

    private var loginFragmentListener: LoginFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginFragmentListener = context as LoginFragmentListener
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_details.visibility = View.VISIBLE
        connected_functions.visibility = View.GONE
        server_url.setText("http://10.60.1.124:9210/ws")

        userOne.setOnClickListener {
            user_id.setText("179677")
            display_name.setText("Aaron")
            token.setText("a57e286b7aab394605c1105dcf01925895c6c6dc")
        }

        userTwo.setOnClickListener {
            user_id.setText("9")
            display_name.setText("Sam")
            token.setText("8dd31ccdce9e10b8738cf23f9a7517130e6ddc97")
        }

        connectBtn.setOnClickListener({
            if (activity.currentFocus != null && activity.currentFocus.windowToken != null) {
                val imm = activity.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
            }

            val isConnected = (activity as TNCBaseActivity).isDinoConnected()

            if (!isConnected) {
                status_box.append("Connecting\n")
                loginFragmentListener?.onConnectToService(server_url.text.toString())
            } else {
                loginFragmentListener?.onDisconnectFromService()
                status_box.append("Disconnected\n")
                user_details.visibility = View.VISIBLE
                connected_functions.visibility = View.GONE
            }
        })

        channel_list.setOnClickListener({
            loginFragmentListener?.goToChannelList()
        })
    }

    fun onConnect() {
        val loginModel = LoginModel(user_id.text.toString().toInt(), display_name.text.toString(), token.text.toString())
        loginFragmentListener?.onLoginToService(loginModel)
        connectBtn.text = getString(R.string.disconnect)
        status_box.append("Connected\n")
    }

    fun loggedIn() {
        user_details.visibility = View.GONE
        connected_functions.visibility = View.VISIBLE
        status_box.append("Logged in Successfully\n")
    }

    fun onError(error: DinoError) {
        if (status_box != null) {
            status_box.append("Error: " + error.toString() + "\n")
            connectBtn.text = getString(R.string.connect)
            user_details.visibility = View.VISIBLE
            connected_functions.visibility = View.GONE
        }
    }
}