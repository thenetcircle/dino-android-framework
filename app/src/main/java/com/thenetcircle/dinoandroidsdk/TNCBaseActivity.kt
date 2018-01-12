package com.thenetcircle.dinoandroidsdk

import android.support.v7.app.AppCompatActivity
import com.thenetcircle.dinoandroidsdk.dino.DinoChatConnection

/**
 * Created by aaron on 11/01/2018.
 */
open class TNCBaseActivity : AppCompatActivity() {
    var dinoChatConnection: DinoChatConnection = TNCApplication.dinoConnection
}