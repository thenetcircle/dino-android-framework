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

package com.thenetcircle.dino.data

/**
 * Created by aaron on 01/02/2018.
 */


val loginModelResultData = "{\n" +
        "    \"status_code\": 200,\n" +
        "    \"data\": {\n" +
        "        \"id\": \"ABC!@#\",\n" +
        "        \"published\": \"2017-08-01 12:00:00\",\n" +
        "        \"actor\": {\n" +
        "            \"id\": \"1234\",\n" +
        "            \"displayName\": \"TEST USER\",\n" +
        "            \"attachments\": [\n" +
        "                {\n" +
        "                    \"objectType\": \"room_role\",\n" +
        "                    \"id\": \"QWER\",\n" +
        "                    \"content\": \"moderator,owner\"\n" +
        "                },\n" +
        "                {\n" +
        "                    \"objectType\": \"room_role\",\n" +
        "                    \"id\": \"QWER\",\n" +
        "                    \"content\": \"owner\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"object\": {\n" +
        "            \"objectType\": \"history\",\n" +
        "            \"attachments\": []\n" +
        "        },\n" +
        "        \"verb\": \"login\"\n" +
        "    }\n" +
        "}"