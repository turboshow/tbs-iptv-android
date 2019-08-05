package cn.turboshow.tv.ui

import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import cn.turboshow.tv.R
import cn.turboshow.tv.ui.iptv.IPTVActivity
import cn.turboshow.tv.ui.presenter.GridItemPresenter

class MainFragment : BrowseSupportFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        setupEventListeners()
        loadData()
    }

    private fun loadData() {
        adapter = ArrayObjectAdapter(ListRowPresenter()).apply {
            add(
                ListRow(
                    HeaderItem(resources.getString(R.string.browse)),
                    ArrayObjectAdapter(GridItemPresenter()).apply {
                        add("USB")
                        add("DLNA")
                        add("SMB")
                        add("NFS")
                    })
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
        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            when (item) {
                "Watch" -> openIPTVActivity()
            }
        }
    }

    private fun openIPTVActivity() {
        startActivity(IPTVActivity.newIntent(activity!!))
    }
}