package cn.turboshow.tv.ui.browse

import android.os.Bundle
import android.os.IBinder
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.VerticalGridPresenter
import cn.turboshow.tv.browse.UpnpDirectoryContainer
import cn.turboshow.tv.browse.UpnpDirectoryItem
import cn.turboshow.tv.service.TBSService
import cn.turboshow.tv.ui.presenter.GridItemPresenter
import cn.turboshow.tv.util.ServiceBinder
import org.fourthline.cling.model.ServiceReference
import org.fourthline.cling.model.action.ActionInvocation
import org.fourthline.cling.model.message.UpnpResponse
import org.fourthline.cling.model.meta.Service
import org.fourthline.cling.support.contentdirectory.callback.Browse
import org.fourthline.cling.support.model.BrowseFlag
import org.fourthline.cling.support.model.DIDLContent

class UpnpBrowseFragment : VerticalGridSupportFragment() {
    private val itemsAdapter = ArrayObjectAdapter(GridItemPresenter())
    private lateinit var serviceBinder: ServiceBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupFragment()
        initService()
    }

    private fun initService() {
        serviceBinder = object : ServiceBinder(context!!, this) {
            override fun onServiceConnected(binder: IBinder) {
                val serviceRef = ServiceReference(arguments!!.getString(ARG_DIRECTORY_SERVICE_REF))
                val containerId = arguments!!.getString(ARG_CONTAINER_ID)
                val service = (binder as TBSService.Binder).getService()
                val upnpService = service.upnpService.registry.getService(serviceRef)
                service.upnpService.controlPoint.execute(object :
                    Browse(upnpService, containerId, BrowseFlag.DIRECT_CHILDREN) {
                    override fun updateStatus(status: Status?) {
                    }

                    override fun received(actionInvocation: ActionInvocation<out Service<*, *>>?, didl: DIDLContent?) {
                        activity?.runOnUiThread {
                            didl?.let {
                                it.containers.forEach { container ->
                                    itemsAdapter.add(UpnpDirectoryContainer(container))
                                }
                                it.items.forEach { item ->
                                    itemsAdapter.add(UpnpDirectoryItem(item))
                                }
                            }
                        }
                    }

                    override fun failure(
                        invocation: ActionInvocation<out Service<*, *>>?,
                        operation: UpnpResponse?,
                        defaultMsg: String?
                    ) {
                    }

                })
            }

            override fun onServiceDisconnected() {
            }

        }
        serviceBinder.bind(TBSService::class.java)
    }

    private fun setupFragment() {
        adapter = itemsAdapter
        title = arguments!!.getString(ARG_TITLE)
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
    }

    companion object {
        private const val ARG_DIRECTORY_SERVICE_REF = "upnp_service_ref"
        private const val ARG_TITLE = "title"
        private const val ARG_CONTAINER_ID = "container_id"
        private const val NUM_COLUMNS = 4

        fun newInstance(title: String, directoryServiceRef: String, containerId: String): UpnpBrowseFragment {
            return UpnpBrowseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DIRECTORY_SERVICE_REF, directoryServiceRef)
                    putString(ARG_CONTAINER_ID, containerId)
                }
            }
        }
    }
}
