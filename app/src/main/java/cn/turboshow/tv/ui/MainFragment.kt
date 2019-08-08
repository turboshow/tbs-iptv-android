package cn.turboshow.tv.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import cn.turboshow.tv.R
import cn.turboshow.tv.ui.iptv.IPTVActivity
import cn.turboshow.tv.ui.presenter.GridItemPresenter
import org.fourthline.cling.android.AndroidUpnpService
import org.fourthline.cling.android.AndroidUpnpServiceImpl
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.registry.RegistryListener
import java.lang.Exception

class MainFragment : BrowseSupportFragment() {
    private var upnpService: AndroidUpnpService? = null
    private val devicesAdapter = ArrayObjectAdapter(GridItemPresenter())
    private val upnpRegistryListener = object : RegistryListener {
        override fun localDeviceRemoved(registry: Registry?, device: LocalDevice?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun remoteDeviceDiscoveryStarted(registry: Registry?, device: RemoteDevice?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun remoteDeviceDiscoveryFailed(registry: Registry?, device: RemoteDevice?, ex: Exception?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun afterShutdown() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun remoteDeviceAdded(registry: Registry?, device: RemoteDevice?) {
            devicesAdapter.add(device!!.displayString)
        }

        override fun remoteDeviceUpdated(registry: Registry?, device: RemoteDevice?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beforeShutdown(registry: Registry?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun remoteDeviceRemoved(registry: Registry?, device: RemoteDevice?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun localDeviceAdded(registry: Registry?, device: LocalDevice?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
    private val upnpServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            upnpService!!.registry.removeListener(upnpRegistryListener)
            upnpService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            upnpService = (service as AndroidUpnpService).also {
                it.registry.addListener(upnpRegistryListener)
                it.controlPoint.search()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        setupEventListeners()
        loadData()
        startScanningDevices()
    }

    private fun startScanningDevices() {
        bindUpnpService()
    }

    private fun bindUpnpService() {
        context?.bindService(
            Intent(context, AndroidUpnpServiceImpl::class.java),
            upnpServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun loadData() {
        adapter = ArrayObjectAdapter(ListRowPresenter()).apply {
            add(
                ListRow(
                    HeaderItem(resources.getString(R.string.browse)), devicesAdapter
                )
            )
            add(
                ListRow(
                    HeaderItem(resources.getString(R.string.iptv)),
                    ArrayObjectAdapter(GridItemPresenter()).apply {
                        add("Watch")
                    })
            )
        }
    }

    private fun setupUIElements() {
        title = resources.getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.colorPrimary)
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                "Watch" -> openIPTVActivity()
            }
        }
    }

    private fun openIPTVActivity() {
        startActivity(IPTVActivity.newIntent(activity!!))
    }

    override fun onDestroy() {
        super.onDestroy()
        upnpService?.let {
            upnpService!!.registry.removeListener(upnpRegistryListener)
            context?.unbindService(upnpServiceConnection)
        }
    }
}