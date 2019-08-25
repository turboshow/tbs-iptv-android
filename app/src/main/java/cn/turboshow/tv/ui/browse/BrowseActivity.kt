package cn.turboshow.tv.ui.browse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.OnItemViewClickedListener
import cn.turboshow.tv.R
import cn.turboshow.tv.browse.UpnpDirectoryContainer
import cn.turboshow.tv.browse.UpnpDirectoryItem
import cn.turboshow.tv.ui.player.PlayerActivity
import org.fourthline.cling.model.meta.RemoteService

class BrowseActivity : FragmentActivity() {
    private lateinit var directoryServiceRef: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        directoryServiceRef = intent.getStringExtra(ARG_DIRECTORY_SERVICE_REF)
        val rootTitle = intent.getStringExtra(ARG_ROOT_TITLE)

        if (savedInstanceState != null) return

        showItems(rootTitle, "0")
    }

    private fun showItems(title: String, containerId: String) {
        val fragment = BrowseFragment.newInstance(title, directoryServiceRef, containerId)
        fragment.setOnItemViewClickedListener(OnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is UpnpDirectoryContainer -> {
                    showItems(item.container.title, item.container.id)
                }
                is UpnpDirectoryItem -> {
                    playItem(item)
                }
            }
        })
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            if (containerId != "0") {
                addToBackStack(null)
            }
        }.commit()
    }

    private fun playItem(item: UpnpDirectoryItem) {
        val title = item.title
        val url = item.item.firstResource.value
        startActivity(PlayerActivity.newIntent(this, title, url))
    }

    companion object {
        private const val ARG_DIRECTORY_SERVICE_REF = "upnp_service_ref"
        private const val ARG_ROOT_TITLE = "root_title"

        fun newIntent(context: Context, rootTitle: String, service: RemoteService): Intent {
            return Intent(context, BrowseActivity::class.java).apply {
                putExtra(ARG_ROOT_TITLE, rootTitle)
                putExtra(ARG_DIRECTORY_SERVICE_REF, service.reference.toString())
            }
        }
    }
}
