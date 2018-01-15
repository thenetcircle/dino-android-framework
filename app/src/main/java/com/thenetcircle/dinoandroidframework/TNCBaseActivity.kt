package com.thenetcircle.dinoandroidframework

import android.support.v7.app.AppCompatActivity
import com.thenetcircle.dinoandroidframework.dino.DinoChatConnection
import com.thenetcircle.dinoandroidframework.dino.model.results.LoginModelResult

/**
 * Created by aaron on 11/01/2018.
 */
open class TNCBaseActivity : AppCompatActivity() {

    companion object {
        var loginObject : LoginModelResult? = null
    }

    var dinoChatConnection: DinoChatConnection = TNCApplication.dinoConnection
}