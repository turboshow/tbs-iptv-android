package cn.turboshow.tv.browse

import android.graphics.drawable.Drawable
import org.fourthline.cling.support.model.container.Container

class UpnpDirectoryContainer(icon: Drawable, val container: Container) :
    BrowseItem(icon, container.title)