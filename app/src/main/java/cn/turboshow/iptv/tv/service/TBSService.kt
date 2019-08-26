package cn.turboshow.iptv.tv.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService
import javax.inject.Inject

class TBSService : DaggerService() {
    @Inject
    lateinit var webServer: WebServer
    private val binder = Binder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        webServer.start()
    }

    override fun onDestroy() {
        webServer.stop()

        super.onDestroy()
    }

    inner class Binder : android.os.Binder() {
        fun getService(): TBSService {
            return this@TBSService
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TBSService::class.java)
        }
    }
}
