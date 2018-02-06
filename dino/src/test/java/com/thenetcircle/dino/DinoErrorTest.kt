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

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

/**
 * Created by aaron on 06/02/2018.
 */

@RunWith(BlockJUnit4ClassRunner::class)
class DinoErrorTest {

    @Test
    fun checkErrorByValueFunction() {
        // valid
        Assert.assertEquals(DinoError.getErrorByCode(600) , DinoError.INVALID_TARGET_TYPE)
        Assert.assertEquals(DinoError.getErrorByCode(700) , DinoError.EMPTY_MESSAGE)
        Assert.assertEquals(DinoError.getErrorByCode(703) , DinoError.USER_IS_BANNED)
        Assert.assertEquals(DinoError.getErrorByCode(100) , DinoError.NO_SOCKET_ERROR)

        //invalid
        Assert.assertEquals(DinoError.getErrorByCode(0) , DinoError.UNKNOWN_ERROR)
        Assert.assertEquals(DinoError.getErrorByCode(-1) , DinoError.UNKNOWN_ERROR)
        Assert.assertEquals(DinoError.getErrorByCode(Int.MIN_VALUE) , DinoError.UNKNOWN_ERROR)
        Assert.assertEquals(DinoError.getErrorByCode(Int.MAX_VALUE) , DinoError.UNKNOWN_ERROR)
    }

}