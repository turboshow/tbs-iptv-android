package cn.turboshow.tv.browse

import org.fourthline.cling.support.model.item.Item

class UpnpDirectoryItem(val item: Item): BrowseItem(item.title)