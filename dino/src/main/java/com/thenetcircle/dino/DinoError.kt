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

/**
 * Created by aaron on 09/01/2018.
 */
enum class DinoError constructor(var errorCode: Int) {
    NO_SOCKET_ERROR(100),
    EVENT_CONNECT_ERROR(101),
    EVENT_DISCONNECT(102),
    LOCAL_NOT_LOGGED_IN(103),

    UNKNOWN_ERROR(250),
    MISSING_ACTOR_ID(500),
    MISSING_OBJECT_ID(501),
    MISSING_TARGET_ID(502),
    MISSING_OBJECT_URL(503),
    MISSING_TARGET_DISPLAY_NAME(504),
    MISSING_ACTOR_URL(505),
    MISSING_OBJECT_CONTENT(506),
    MISSING_OBJECT(507),
    MISSING_OBJECT_ATTACHMENTS(508),
    MISSING_ATTACHMENT_TYPE(509),
    MISSING_ATTACHMENT_CONTENT(510),

    INVALID_TARGET_TYPE(600),
    INVALID_ACL_TYPE(601),
    INVALID_ACL_ACTION(602),
    INVALID_ACL_VALUE(603),
    INVALID_STATUS(604),
    INVALID_OBJECT_TYPE(605),
    INVALID_BAN_DURATION(606),

    EMPTY_MESSAGE(700),
    NOT_BASE64(701),
    USER_NOT_IN_ROOM(702),
    USER_IS_BANNED(703),
    ROOM_ALREADY_EXISTS(704),
    NOT_ALLOWED(705),
    VALIDATION_ERROR(706),
    ROOM_FULL(707),
    NOT_ONLINE(708),
    TOO_MANY_PRIVATE_ROOMS(709),

    ROOM_NAME_TOO_SHORT(711),
    INVALID_TOKEN(712),
    INVALID_LOGIN(713),
    MSG_TOO_LONG(714),
    MULTIPLE_ROOMS_WITH_NAME(715),
    TOO_MANY_ATTACHMENTS(716),
    NOT_ENABLED(717),

    NO_SUCH_USER(800),
    NO_SUCH_CHANNEL(801),
    NO_SUCH_ROOM(802),
    NO_ADMIN_ROOM_FOUND(803),
    NO_USER_IN_SESSION(804),
    NO_ADMIN_ONLINE(805);

    companion object {
        fun getErrorByCode(code: Int): DinoError {
            for (i in 0 until values().size) {
                if (values()[i].errorCode == code) {
                    return values()[i]
                }
            }
            return UNKNOWN_ERROR
        }
    }
}