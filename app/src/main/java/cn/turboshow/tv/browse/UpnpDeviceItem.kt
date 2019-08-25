package cn.turboshow.tv.browse

import android.graphics.drawable.ColorDrawable
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.model.meta.RemoteService

class UpnpDeviceItem(val device: RemoteDevice, val service: RemoteService) :
    BrowseItem(ColorDrawable(), device.displayString)