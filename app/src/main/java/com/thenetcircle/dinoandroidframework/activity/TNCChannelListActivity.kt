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
import com.thenetcircle.dinoandroidframework.dino.DinoError
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoChannelListListener
import com.thenetcircle.dinoandroidframework.dino.interfaces.DinoErrorListener
import com.thenetcircle.dinoandroidframework.dino.model.data.ChannelListModel
import com.thenetcircle.dinoandroidframework.dino.model.results.ChannelListModelResult
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
        dinoChatConnection.getChannelList(ChannelListModel(), this, this)
        tncChannelListFragment.updatingChannels()
    }

    override fun onResult(result: ChannelListModelResult) {
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