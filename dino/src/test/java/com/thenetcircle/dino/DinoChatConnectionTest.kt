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

package com.thenetcircle.dino

import android.os.Looper
import com.thenetcircle.dino.interfaces.DinoConnectionListener
import com.thenetcircle.dino.interfaces.DinoErrorListener
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by aaron on 19/01/2018.
 */

@RunWith(PowerMockRunner::class)
@PrepareForTest(Looper::class)
class DinoChatConnectionTest {
    var dinoChatConnection: DinoChatConnection = DinoChatConnection()

    @Mock
    var dinoErrorListener = Mockito.mock(DinoErrorListener::class.java)

    @Mock
    var dinoConnectionListener = Mockito.mock(DinoConnectionListener::class.java)

    @Mock
    var socket = Mockito.mock(Socket::class.java)

    @Mock
    var looper = Mockito.mock(Looper::class.java)

    @Before
    fun start() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(Looper::class.java)
        Mockito.`when`(Looper.getMainLooper()).thenReturn(looper)
        Mockito.`when`(Looper.myLooper()).thenReturn(looper)
    }

    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)

    @Test
    fun checkConnectionCallback() {
        dinoChatConnection.connectionListener = dinoConnectionListener
        dinoChatConnection.startConnection(socket, dinoErrorListener)

        Mockito.verify(socket).on(Mockito.eq(Socket.EVENT_DISCONNECT), Mockito.any())
        Mockito.verify(socket).on(Mockito.eq(Socket.EVENT_CONNECT_ERROR), Mockito.any())
        Mockito.verify(socket).connect()

        val captor = argumentCaptor<Emitter.Listener>()
        Mockito.verify(socket).on(Mockito.eq("gn_connect"), captor.capture())
        captor.value.call()
        Mockito.verify(dinoConnectionListener).onConnect()
    }

    @Test
    fun checkConnectionDisconnect() {
        dinoChatConnection.connectionListener = dinoConnectionListener
        dinoChatConnection.startConnection(socket, dinoErrorListener)
        dinoChatConnection.disconnect()
        Mockito.verify(socket).off(Mockito.eq(Socket.EVENT_DISCONNECT))
        Mockito.verify(socket).off(Mockito.eq(Socket.EVENT_CONNECT_ERROR))
        Mockito.verify(socket).disconnect()
    }

}
