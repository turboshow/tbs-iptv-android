package cn.turboshow.tv.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.turboshow.tv.R

class PlayerActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        if (savedInstanceState != null) return

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, PlayerFragment.newInstance(
            intent.getStringExtra(ARG_TITLE),
            intent.getStringExtra(ARG_URI)
        )).commit()
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_URI = "uri"

        fun newIntent(context: Context, title: String, uri: String): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(ARG_TITLE, title)
                putExtra(ARG_URI, uri)
            }
        }
    }
}