package com.thenetcircle.dinoandroidframework

import android.content.Intent
import android.os.Bundle
import com.thenetcircle.dinoandroidframework.dino.DinoError
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoLoginListener
import com.thenetcircle.dinoandroidframework.dino.model.data.LoginModel
import com.thenetcircle.dinoandroidframework.dino.model.results.LoginModelResult
import com.thenetcircle.dinoandroidframework.fragment.TNCLoginFragment

class TNCMainActivity : TNCBaseActivity(), TNCLoginFragment.LoginFragmentListener, DinoLoginListener, DinoErrorListener {

    private val loginFragment = TNCLoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTrans(loginFragment)
    }

    override fun onConnectToService(url: String) {
        dinoChatConnection.startConnection(url, this)
    }

    override fun onDisconnectFromService() {
        dinoChatConnection.disconnect()
    }

    override fun onLoginToService(loginModel: LoginModel) {
        dinoChatConnection.login(loginModel, this, this)
    }

    override fun goToChannelList() {
        val intent = Intent(this, TNCChannelListActivity::class.java)
        startActivity(intent)
    }

    override fun onConnect() {
        super.onConnect()
        loginFragment.onConnect()
    }

    override fun onError(error: DinoError) {
        loginFragment.onError(error)
        dinoChatConnection.disconnect()

    }

    override fun onResult(result: LoginModelResult) {
        loginObject = result
        loginFragment.loggedIn()
    }
}
