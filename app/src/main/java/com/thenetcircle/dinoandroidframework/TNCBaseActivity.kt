package com.thenetcircle.dinoandroidframework

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.thenetcircle.dinoandroidframework.dino.DinoChatConnection
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoConnectionListener
import com.thenetcircle.dinoandroidframework.dino.model.results.LoginModelResult

/**
 * Created by aaron on 11/01/2018.
 */
open class TNCBaseActivity : AppCompatActivity(), DinoConnectionListener {
    companion object {
        var loginObject: LoginModelResult? = null
    }

    var dinoChatConnection: DinoChatConnection = TNCApplication.dinoConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dinoChatConnection.connectionListener = this
    }

    protected fun fragmentTrans(frag: Fragment) {
        fragmentManager.beginTransaction()
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

}