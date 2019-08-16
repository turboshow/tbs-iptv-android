package cn.turboshow.tv.ui.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.turboshow.tv.R
import cn.turboshow.tv.TBSPlayer
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : FragmentActivity() {
    private lateinit var player: TBSPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)
        player = TBSPlayer(this, videoView)
    }

    private fun play() {
        val url = intent.getStringExtra(ARG_URL)
        player.play(Uri.parse(url))
    }

    private fun stop() {
        player.stop()
    }

    override fun onStart() {
        super.onStart()

        play()
    }

    override fun onStop() {
        super.onStop()

        stop()
    }

    override fun onDestroy() {
        player.release()

        super.onDestroy()
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_URL = "url"

        fun newIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(ARG_TITLE, title)
                putExtra(ARG_URL, url)
            }
        }
    }
}