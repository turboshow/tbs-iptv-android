package cn.turboshow.tv.ui.browse

import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.leanback.widget.OnItemViewClickedListener
import cn.turboshow.tv.R
import cn.turboshow.tv.browse.UpnpDirectoryContainer
import cn.turboshow.tv.browse.UpnpDirectoryItem
import cn.turboshow.tv.service.TBSService
import cn.turboshow.tv.util.ServiceBinder
import kotlinx.android.synthetic.main.fragment_browse.view.*
import org.fourthline.cling.model.ServiceReference
import org.fourthline.cling.model.action.ActionInvocation
import org.fourthline.cling.model.message.UpnpResponse
import org.fourthline.cling.model.meta.Service
import org.fourthline.cling.support.contentdirectory.callback.Browse
import org.fourthline.cling.support.model.BrowseFlag
import org.fourthline.cling.support.model.DIDLContent

class BrowseFragment : Fragment() {
    private val itemsAdapter = BrowserItemsAdapter()
    private lateinit var serviceBinder: ServiceBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                    override fun received(
                        actionInvocation: ActionInvocation<out Service<*, *>>?,
                        didl: DIDLContent?
                    ) {
                        activity?.runOnUiThread {
                            didl?.let {
                                it.containers.forEach { container ->
                                    itemsAdapter.add(
                                        UpnpDirectoryContainer(
                                            context!!.getDrawable(R.drawable.ic_folder)!!,
                                            container
                                        )
                                    )
                                }
                                it.items.forEach { item ->
                                    itemsAdapter.add(
                                        UpnpDirectoryItem(
                                            context!!.getDrawable(R.drawable.ic_file)!!,
                                            item
                                        )
                                    )
                                }
                            }
                            itemsAdapter.notifyDataSetChanged()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browse, container, false).apply {
            titleView.text = arguments?.getString(ARG_TITLE)
            itemsView.run {
                setNumColumns(1)
                verticalSpacing = 0
                setBackgroundColor(Color.parseColor("#111111"))
                adapter = itemsAdapter
            }
        }
    }

    fun setOnItemViewClickedListener(listener: OnItemViewClickedListener) {
        itemsAdapter.onItemViewClickedListener = listener
    }

    companion object {
        private const val ARG_DIRECTORY_SERVICE_REF = "upnp_service_ref"
        private const val ARG_TITLE = "title"
        private const val ARG_CONTAINER_ID = "container_id"

        fun newInstance(
            title: String,
            directoryServiceRef: String,
            containerId: String
        ): BrowseFragment {
            return BrowseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DIRECTORY_SERVICE_REF, directoryServiceRef)
                    putString(ARG_CONTAINER_ID, containerId)
                }
            }
        }
    }
}
