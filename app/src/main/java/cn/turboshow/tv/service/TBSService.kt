package cn.turboshow.tv.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService
import org.fourthline.cling.UpnpService
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.android.AndroidRouter
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration
import org.fourthline.cling.protocol.ProtocolFactory
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.transport.Router
import javax.inject.Inject

class TBSService : DaggerService() {
    @Inject
    lateinit var webServer: WebServer
    lateinit var upnpService: UpnpService
    private val binder = Binder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        webServer.start()
        initUpnpService()
    }

    private fun initUpnpService() {
        upnpService = object : UpnpServiceImpl(AndroidUpnpServiceConfiguration()) {
            override fun createRouter(protocolFactory: ProtocolFactory?, registry: Registry?): Router {
                return AndroidRouter(configuration, protocolFactory, this@TBSService)
            }

            override fun shutdown() {
                (router as AndroidRouter).unregisterBroadcastReceiver()
                super.shutdown()
            }
        }
    }

    override fun onDestroy() {
        webServer.stop()
        upnpService.shutdown()

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
