package com.thenetcircle.dinoandroidsdk

import android.app.Application
import com.thenetcircle.dinoandroidsdk.dino.DinoChatConnection

/**
 * Created by aaron on 11/01/2018.
 */
class TNCApplication : Application() {
    companion object {
        var dinoConnection: DinoChatConnection = DinoChatConnection()
    }

}