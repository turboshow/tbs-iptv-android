package cn.turboshow.tv.ui

import android.os.Bundle
import android.os.IBinder
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import cn.turboshow.tv.R
import cn.turboshow.tv.browse.IptvItem
import cn.turboshow.tv.browse.UpnpDeviceItem
import cn.turboshow.tv.service.TBSService
import cn.turboshow.tv.ui.browse.UpnpBrowseActivity
import cn.turboshow.tv.ui.iptv.IptvActivity
import cn.turboshow.tv.ui.presenter.GridItemPresenter
import cn.turboshow.tv.util.ServiceBinder
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.registry.RegistryListener

private const val UPNP_SERVICE_TYPE_CONTENT_DIRECTORY = "ContentDirectory"

class MainFragment : BrowseSupportFragment() {
    private var tbsService: TBSService? = null
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

    private lateinit var serviceBinder: ServiceBinder

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        setupEventListeners()
        loadData()
        initService()
    }

    private fun initService() {
        serviceBinder = object : ServiceBinder(activity!!, this) {
            override fun onServiceConnected(binder: IBinder) {
                tbsService = (binder as TBSService.Binder).getService().also {
                    it.upnpService.let { upnpService ->
                        upnpService.registry.remoteDevices.forEach(::onUpnpDeviceAdded)
                        upnpService.registry.addListener(upnpRegistryListener)
                        upnpService.controlPoint.search()
                    }
                }
            }

            override fun onServiceDisconnected() {
                tbsService?.upnpService?.registry?.removeListener(upnpRegistryListener)
                tbsService = null
            }
        }
        serviceBinder.bind(TBSService::class.java)
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
                        add(IptvItem())
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
                is IptvItem -> {
                    context!!.startActivity(IptvActivity.newIntent(context!!))
                }
                is UpnpDeviceItem -> {
                    context!!.startActivity(
                        UpnpBrowseActivity.newIntent(
                            context!!,
                            item.device.displayString,
                            item.service
                        )
                    )
                }
            }
        }
    }

    private fun onUpnpDeviceAdded(device: RemoteDevice) {
        activity!!.runOnUiThread {
            if (device !in devicesAdapter.unmodifiableList<RemoteDevice>()) {
                for (service in device.services) {
                    if (service.serviceType.type == UPNP_SERVICE_TYPE_CONTENT_DIRECTORY) {
                        devicesAdapter.add(UpnpDeviceItem(device, service))
                        break
                    }
                }
            }
        }
    }
}