package cn.turboshow.tv.browse

import android.graphics.drawable.Drawable
import org.fourthline.cling.support.model.item.Item

class UpnpDirectoryItem(icon: Drawable, val item: Item): BrowseItem(icon, item.title)