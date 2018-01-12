package com.thenetcircle.dinoandroidsdk.dino.interfaces

import com.thenetcircle.dinoandroidsdk.dino.DinoError

/**
 * Created by aaron on 11/01/2018.
 */
interface DinoErrorListener {
    fun onError(error: DinoError)
}