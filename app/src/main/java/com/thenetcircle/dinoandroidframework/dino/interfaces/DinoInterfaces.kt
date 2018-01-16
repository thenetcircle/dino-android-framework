/*
 * Copyright 2018 The NetCircle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thenetcircle.dinoandroidframework.dino.interfaces

import com.thenetcircle.dinoandroidframework.dino.model.results.*

/**
 * Created by aaron on 12/01/2018.
 */

interface DinoParentInterface<in T : ModelResultParent> {
    fun onResult(result: T)
}

interface DinoLoginListener : DinoParentInterface<LoginModelResult>

interface DinoRoomEntryListener : DinoParentInterface<RoomListModelResult>

interface DinoChannelListListener : DinoParentInterface<ChannelListModelResult>

interface DinoRoomCreationListener : DinoParentInterface<RoomCreateResultModel>

interface DinoJoinRoomListener : DinoParentInterface<JoinRoomResultModel>

interface DinoChatHistoryListener : DinoParentInterface<ChatHistoryResult>