package cn.turboshow.tv.ui.player

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import cn.turboshow.tv.player.TBSPlayer

class PlayerFragment : VideoSupportFragment() {
    private var player: TBSPlayer? = null
    private var playerAdapter: TBSPlayerAdapter? = null
    private var playerGlue: PlaybackTransportControlGlue<PlayerAdapter>? = null
    private lateinit var uri: Uri

    override fun onStart() {
        super.onStart()

        uri = Uri.parse(arguments!!.getString(ARG_URI))
        initPlayer()
    }

    override fun onStop() {
        releasePlayer()

        super.onStop()
    }

    private fun initPlayer() {
        player = TBSPlayer(context!!)
        player!!.setMedia(uri)
        player!!.setVideoView(surfaceView)

        playerAdapter = TBSPlayerAdapter(player!!)
        playerGlue = PlaybackTransportControlGlue(context!!, playerAdapter!!)
        playerGlue?.let {
            it.title = arguments!!.getString(ARG_TITLE)
            it.isControlsOverlayAutoHideEnabled = true
            it.host = VideoSupportFragmentGlueHost(this)
        }

        val savedPosition = loadSavedPosition()
        if (savedPosition > 0) {
            player!!.setStartPosition(savedPosition)
        }
        player!!.play()
    }

    private fun savePosition(position: Long) {
        context!!.getSharedPreferences(PREF_SAVED_POSITIONS, Context.MODE_PRIVATE).edit().apply {
            putLong(uri.toString(), position)
        }.apply()
    }

    private fun loadSavedPosition(): Long {
        return context!!.getSharedPreferences(PREF_SAVED_POSITIONS, Context.MODE_PRIVATE).getLong(uri.toString(), 0)
    }

    private fun releasePlayer() {
        savePosition(player!!.getPosition())
        player?.let {
            player!!.release()
            player = null
            playerAdapter = null
            playerGlue = null
        }
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_URI = "uri"

        private const val PREF_SAVED_POSITIONS = "position"

        fun newInstance(title: String, uri: String): PlayerFragment {
            return PlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_URI, uri)
                }
            }
        }
    }
}