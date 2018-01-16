package com.thenetcircle.dinoandroidframework

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