package com.thenetcircle.dinoandroidsdk.dino.interfaces

import com.thenetcircle.dinoandroidsdk.dino.model.results.ModelResultParent

/**
 * Created by aaron on 12/01/2018.
 */
interface DinoParentInterface<in T : ModelResultParent> {
    fun onResult(result: T)
}

