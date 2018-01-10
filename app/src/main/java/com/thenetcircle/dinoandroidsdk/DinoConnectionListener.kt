package com.thenetcircle.dinoandroidsdk

import com.thenetcircle.dinoandroidsdk.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidsdk.model.results.LoginModelResult
import com.thenetcircle.dinoandroidsdk.model.results.RoomListModelResult

/**
 * Created by aaron on 09/01/2018.
 */
interface DinoConnectionListener {
    fun onConnection()

    fun onError(error: DinoError)

    fun onLogin(loginModelResult: LoginModelResult)

    fun onChannelListReceived(channelListModelResult: ChannelListModelResult)

    fun onChannelRoomReceived(roomListModelResult: RoomListModelResult)
}