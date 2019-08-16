package cn.turboshow.tv.browse

import org.fourthline.cling.support.model.container.Container

class UpnpDirectoryContainer(val container: Container): BrowseItem(container.title)