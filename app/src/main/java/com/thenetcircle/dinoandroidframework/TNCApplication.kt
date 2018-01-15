package com.thenetcircle.dinoandroidframework

import android.app.Application
import com.thenetcircle.dinoandroidframework.dino.DinoChatConnection

/**
 * Created by aaron on 11/01/2018.
 */
class TNCApplication : Application() {
    companion object {
        var dinoConnection: DinoChatConnection = DinoChatConnection()
    }

}