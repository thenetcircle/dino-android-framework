package com.thenetcircle.dinoandroidframework.dino.interfaces

import com.thenetcircle.dinoandroidframework.dino.DinoError

/**
 * Created by aaron on 11/01/2018.
 */
interface DinoErrorListener {
    fun onError(error: DinoError)
}