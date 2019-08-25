package cn.turboshow.tv.ui.browse

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.recyclerview.widget.RecyclerView
import cn.turboshow.tv.R
import cn.turboshow.tv.browse.BrowseItem

class BrowserItemsAdapter() :
    RecyclerView.Adapter<BrowserItemsAdapter.ViewHolder>() {
    private val items = mutableListOf<BrowseItem>()
    var onItemViewClickedListener: OnItemViewClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_browse, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setIcon(item.icon)
        holder.setTitle(item.title)
        holder.itemView.setOnClickListener {
            if (onItemViewClickedListener != null) {
                onItemViewClickedListener!!.onItemClicked(null, item, null, null)
            }
        }
    }

    fun add(item: BrowseItem) {
        items.add(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.iconView)
        private val titleView: TextView = itemView.findViewById(R.id.titleView)

        fun setIcon(icon: Drawable) {
            iconView.setImageDrawable(icon)
        }

        fun setTitle(title: String) {
            titleView.text = title
        }
    }
}