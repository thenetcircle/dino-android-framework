package com.thenetcircle.dinoandroidframework

import android.support.v7.app.AppCompatActivity
import com.thenetcircle.dinoandroidframework.dino.DinoChatConnection

/**
 * Created by aaron on 11/01/2018.
 */
open class TNCBaseActivity : AppCompatActivity() {
    var dinoChatConnection: DinoChatConnection = TNCApplication.dinoConnection
}