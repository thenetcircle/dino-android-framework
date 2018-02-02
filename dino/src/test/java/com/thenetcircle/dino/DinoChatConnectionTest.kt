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
import android.util.Base64
import com.google.gson.Gson
import com.thenetcircle.dino.data.loginModelResultData
import com.thenetcircle.dino.interfaces.DinoConnectionListener
import com.thenetcircle.dino.interfaces.DinoErrorListener
import com.thenetcircle.dino.interfaces.DinoLoginListener
import com.thenetcircle.dino.model.data.LoginModel
import com.thenetcircle.dino.model.results.LoginModelResult
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
@PrepareForTest(Looper::class, Base64::class)
class DinoChatConnectionTest {
    var dinoChatConnection: DinoChatConnection = DinoChatConnection()

    @Mock
    var dinoErrorListener = Mockito.mock(DinoErrorListener::class.java)

    @Mock
    var dinoConnectionListener = Mockito.mock(DinoConnectionListener::class.java)

    @Mock
    var dinoLoginListener = Mockito.mock(DinoLoginListener::class.java)

    @Mock
    var socket = Mockito.mock(Socket::class.java)

    @Mock
    var looper = Mockito.mock(Looper::class.java)

    @Before
    fun start() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(Looper::class.java)
        PowerMockito.mockStatic(Base64::class.java)
        Mockito.`when`(Looper.getMainLooper()).thenReturn(looper)
        Mockito.`when`(Looper.myLooper()).thenReturn(looper)

        dinoChatConnection.connectionListener = dinoConnectionListener
    }

    @Test
    fun checkConnectionCallback() {
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
    fun checkConnectionErrorCallback() {
        dinoChatConnection.startConnection(socket, dinoErrorListener)
        val captor = argumentCaptor<Emitter.Listener>()
        Mockito.verify(socket).on(Mockito.eq(Socket.EVENT_CONNECT_ERROR), captor.capture())
        captor.value.call()
        val error = DinoError.EVENT_CONNECT_ERROR
        Mockito.verify(dinoErrorListener).onError(error)
        Mockito.verify(dinoConnectionListener).onDisconnect()
    }

    @Test
    fun checkConnectionDisconnectCallback() {
        dinoChatConnection.startConnection(socket, dinoErrorListener)
        val captor = argumentCaptor<Emitter.Listener>()
        Mockito.verify(socket).on(Mockito.eq(Socket.EVENT_DISCONNECT), captor.capture())
        captor.value.call()
        val error = DinoError.EVENT_DISCONNECT
        Mockito.verify(dinoErrorListener).onError(error)
        Mockito.verify(dinoConnectionListener).onDisconnect()
    }


    @Test
    fun checkConnectionDisconnect() {
        dinoChatConnection.startConnection(socket, dinoErrorListener)
        dinoChatConnection.disconnect()
        Mockito.verify(socket).off(Mockito.eq(Socket.EVENT_DISCONNECT))
        Mockito.verify(socket).off(Mockito.eq(Socket.EVENT_CONNECT_ERROR))
        Mockito.verify(socket).disconnect()
    }

    @Test
    fun checkLogin() {
        Mockito.`when`(Base64.encodeToString("TEST USER".toByteArray(), Base64.NO_WRAP)).thenReturn("VEVTVCBVU0VS")
        val loginModel = LoginModel(1234 , "TEST USER", "ABCDEFGHI")
        dinoChatConnection.startConnection(socket, dinoErrorListener)
        dinoChatConnection.login(loginModel,dinoLoginListener, dinoErrorListener)


        val emitterCaptor = argumentCaptor<Emitter.Listener>()
        Mockito.verify(socket).on(Mockito.eq("gn_login"), emitterCaptor.capture())

        val requestCaptor = argumentCaptor<JSONObject>()
        Mockito.verify(socket).emit(Mockito.eq("login") , requestCaptor.capture())
        val model = requestCaptor.value

        Assert.assertTrue(model.toString() == JSONObject(Gson().toJson(loginModel)).toString())
        emitterCaptor.value.call(loginModelResultData)

        Mockito.verify(socket).off(Mockito.eq("gn_login"), Mockito.eq(emitterCaptor.value))
        val resultCaptor = argumentCaptor<LoginModelResult>()
        Mockito.verify(dinoLoginListener).onResult(com.thenetcircle.dino.capture(resultCaptor))

        val gson = Gson()
        val obj = gson.fromJson<LoginModelResult>(loginModelResultData, LoginModelResult::class.java)

        Assert.assertTrue(gson.toJson(resultCaptor.value).toString() == gson.toJson(obj).toString())
    }

    @Test
    fun checkGeneralChecks() {
        Assert.assertFalse(dinoChatConnection.generalChecks(dinoErrorListener))
        var error = DinoError.NO_SOCKET_ERROR
        Mockito.verify(dinoErrorListener).onError(error)

        dinoChatConnection.startConnection(socket, dinoErrorListener)

        Assert.assertFalse(dinoChatConnection.generalChecks(dinoErrorListener))
        error = DinoError.LOCAL_NOT_LOGGED_IN
        Mockito.verify(dinoErrorListener).onError(error)

        dinoChatConnection.currentLoggedInUser = LoginModelResult()

        Assert.assertTrue(dinoChatConnection.generalChecks(dinoErrorListener))
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkListenerIsExplicitlySet() {
        dinoChatConnection.connectionListener = null
        dinoChatConnection.startConnection(socket, dinoErrorListener)
    }
}
