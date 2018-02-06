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

package com.thenetcircle.dinoandroidframework.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.thenetcircle.dino.DinoError
import com.thenetcircle.dino.interfaces.DinoChannelListListener
import com.thenetcircle.dino.interfaces.DinoErrorListener
import com.thenetcircle.dino.model.data.RequestChannelList
import com.thenetcircle.dino.model.results.RequestChannelListResult
import com.thenetcircle.dinoandroidframework.fragment.TNCChannelListFragment

/**
 * Created by aaron on 11/01/2018.
 */
class TNCChannelListActivity : TNCBaseActivity(), DinoChannelListListener, DinoErrorListener, TNCChannelListFragment.ChannelListListener {
    private val tncChannelListFragment = TNCChannelListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTrans(tncChannelListFragment)
    }

    override fun onResume() {
        super.onResume()
        dinoChatConnection.getChannelList(RequestChannelList(), this, this)
        tncChannelListFragment.updatingChannels()
    }

    override fun onResult(result: RequestChannelListResult) {
        tncChannelListFragment.channelResult(result)
    }

    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onChannelSelected(id: String) {
        val intent = Intent(this, TNCRoomListActivity::class.java)
        intent.putExtra(TNCRoomListActivity.CHANNELID, id)
        startActivity(intent)
    }
}