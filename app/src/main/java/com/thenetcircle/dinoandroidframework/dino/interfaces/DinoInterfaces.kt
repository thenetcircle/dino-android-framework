package com.thenetcircle.dinoandroidframework.dino.interfaces

import com.thenetcircle.dinoandroidframework.dino.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidframework.dino.model.results.LoginModelResult
import com.thenetcircle.dinoandroidframework.dino.model.results.ModelResultParent
import com.thenetcircle.dinoandroidframework.dino.model.results.RoomListModelResult

/**
 * Created by aaron on 12/01/2018.
 */

interface DinoParentInterface<in T : ModelResultParent> {
    fun onResult(result: T)
}

interface DinoLoginListener : DinoParentInterface<LoginModelResult>

interface DinoRoomEntryListener : DinoParentInterface<RoomListModelResult>

interface DinoChannelListListener : DinoParentInterface<ChannelListModelResult>