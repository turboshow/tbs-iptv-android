package cn.turboshow.tv.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService
import io.reactivex.subjects.Subject
import org.fourthline.cling.model.meta.RemoteDevice
import javax.inject.Inject

class TBSService : DaggerService() {
    @Inject
    lateinit var webServer: WebServer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()

        webServer.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        webServer.stop()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TBSService::class.java)
        }
    }
}
