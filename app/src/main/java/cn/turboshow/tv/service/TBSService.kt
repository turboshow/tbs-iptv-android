package cn.turboshow.tv.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService
import javax.inject.Inject

class TBSService : DaggerService() {
    @Inject
    lateinit var upnpService: UpnpService
    @Inject
    lateinit var webServer: WebServer

    override fun onCreate() {
        super.onCreate()

        upnpService.start()
        webServer.start()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        upnpService.stop()
        webServer.stop()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TBSService::class.java)
        }
    }
}
