package cn.turboshow.tv.browse

class BrowseItem(private val title: String, val onSelected: () -> Unit) {
    override fun toString(): String {
        return title
    }
}
