package com.thenetcircle.dinoandroidframework.fragment

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.thenetcircle.dinoandroidframework.R
import com.thenetcircle.dinoandroidframework.dino.model.results.ChannelAttachment
import com.thenetcircle.dinoandroidframework.dino.model.results.ChannelListModelResult
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