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

package com.thenetcircle.dinoandroidframework.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.thenetcircle.dino.model.results.ChannelAttachment
import com.thenetcircle.dino.model.results.ChannelListModelResult
import com.thenetcircle.dinoandroidframework.R
import kotlinx.android.synthetic.main.fragment_channel_list.*
import java.nio.charset.Charset

/**
 * Created by aaron on 16/01/2018.
 */

class TNCChannelListFragment : Fragment() {

    interface ChannelListListener {
        fun onChannelSelected(id: String)
    }

    private var channelListListener: ChannelListListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_channel_list, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        channelListListener = context as ChannelListListener
    }

    fun updatingChannels() {
        progress_wheel.visibility = View.VISIBLE
        channel_list_container.removeAllViews()
    }

    fun channelResult(result: ChannelListModelResult) {
        progress_wheel.visibility = View.GONE
        for (ob: ChannelAttachment in result.data!!.channels.attachments) {

            val b = Button(activity)
            b.text = String(Base64.decode(ob.displayName, Base64.NO_WRAP), Charset.defaultCharset())
            b.tag = ob.id

            b.setOnClickListener {
                channelListListener?.onChannelSelected(b.tag as String)
            }

            channel_list_container.addView(b)
        }
    }

}