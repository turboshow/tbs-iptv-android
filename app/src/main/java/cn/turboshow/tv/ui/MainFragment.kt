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
import cn.turboshow.tv.browse.BrowseItem
import cn.turboshow.tv.ui.iptv.IPTVActivity
import cn.turboshow.tv.ui.presenter.GridItemPresenter
import org.fourthline.cling.android.AndroidUpnpService
import org.fourthline.cling.android.AndroidUpnpServiceImpl
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.registry.RegistryListener

private const val SERVICE_TYPE_CONTENT_DIRECTORY = "ContentDirectory"

class MainFragment : BrowseSupportFragment() {
    private var upnpService: AndroidUpnpService? = null
    private val devicesAdapter = ArrayObjectAdapter(GridItemPresenter())
    private val upnpRegistryListener = object : RegistryListener {
        override fun localDeviceRemoved(registry: Registry?, device: LocalDevice?) {
        }

        override fun remoteDeviceDiscoveryStarted(registry: Registry?, device: RemoteDevice?) {
        }

        override fun remoteDeviceDiscoveryFailed(registry: Registry?, device: RemoteDevice?, ex: Exception?) {
        }

        override fun afterShutdown() {
        }

        override fun remoteDeviceAdded(registry: Registry?, device: RemoteDevice?) {
            onUpnpDeviceAdded(device!!)
        }

        override fun remoteDeviceUpdated(registry: Registry?, device: RemoteDevice?) {
        }

        override fun beforeShutdown(registry: Registry?) {
        }

        override fun remoteDeviceRemoved(registry: Registry?, device: RemoteDevice?) {
            activity!!.runOnUiThread {
                devicesAdapter.remove(device)
            }
        }

        override fun localDeviceAdded(registry: Registry?, device: LocalDevice?) {
        }

    }
    private val upnpServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            upnpService!!.registry.removeListener(upnpRegistryListener)
            upnpService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            upnpService = (service as AndroidUpnpService).also {
                it.registry.remoteDevices.forEach(::onUpnpDeviceAdded)
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
                        add(BrowseItem(resources.getString(R.string.watch), ::openIPTVActivity))
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
            (item as BrowseItem).onSelected()
        }
    }

    private fun onUpnpDeviceAdded(device: RemoteDevice) {
        activity!!.runOnUiThread {
            if (device !in devicesAdapter.unmodifiableList<RemoteDevice>()) {
                for (upnpService in device.services) {
                    if (upnpService.serviceType.type == SERVICE_TYPE_CONTENT_DIRECTORY) {
                        devicesAdapter.add(BrowseItem(device.displayString, ::openBrowseActivity))
                        break
                    }
                }
            }
        }
    }

    private fun openBrowseActivity() {
        startActivity(BrowseActivity.newIntent(activity!!))
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