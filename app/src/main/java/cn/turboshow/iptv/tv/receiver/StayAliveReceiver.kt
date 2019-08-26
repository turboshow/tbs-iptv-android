package cn.turboshow.iptv.tv.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.turboshow.iptv.tv.service.TBSService

class StayAliveReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(TBSService.newIntent(context))
    }

}