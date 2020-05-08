package cn.turboshow.iptv.tv.ui.iptv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import cn.turboshow.iptv.tv.AppViewModel
import cn.turboshow.iptv.tv.R
import cn.turboshow.iptv.tv.di.activityViewModelProvider
import cn.turboshow.iptv.tv.model.Channel
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_channels.*
import javax.inject.Inject

class ChannelSelectorFragment : DaggerDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChannelsView()
    }

    private fun initChannelsView() {
        viewModel.channels.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            channelsView.adapter = ChannelsAdapter(it).apply {
                onItemViewClickedListener = OnItemViewClickedListener { _, channel, _, _ ->
                    viewModel.selectChannel(it.indexOf(channel))
                    dismiss()
                }
            }
            channelsView.post { channelsView.scrollToPosition(it.indexOf(viewModel.currentChannel.value)) }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChannelSelectorFragment()
    }
}

class ChannelsAdapter(private val channels: List<Channel>) :
    RecyclerView.Adapter<ChannelsAdapter.ViewHolder>() {
    var onItemViewClickedListener: OnItemViewClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_channel, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = channels[position]
        holder.setTitle(channel.title)
        holder.itemView.setOnClickListener {
            if (onItemViewClickedListener != null) {
                onItemViewClickedListener!!.onItemClicked(null, channel, null, null)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setTitle(title: String) {
            (itemView as TextView).text = title
        }
    }
}

