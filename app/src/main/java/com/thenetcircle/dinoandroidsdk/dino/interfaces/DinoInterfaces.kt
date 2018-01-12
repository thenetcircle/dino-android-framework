package com.thenetcircle.dinoandroidsdk.dino.interfaces

import com.thenetcircle.dinoandroidsdk.dino.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidsdk.dino.model.results.LoginModelResult
import com.thenetcircle.dinoandroidsdk.dino.model.results.ModelResultParent
import com.thenetcircle.dinoandroidsdk.dino.model.results.RoomListModelResult

/**
 * Created by aaron on 12/01/2018.
 */

interface DinoParentInterface<in T : ModelResultParent> {
    fun onResult(result: T)
}

interface DinoLoginListener : DinoParentInterface<LoginModelResult>

interface DinoRoomEntryListener : DinoParentInterface<RoomListModelResult>

interface DinoChannelListListener : DinoParentInterface<ChannelListModelResult>