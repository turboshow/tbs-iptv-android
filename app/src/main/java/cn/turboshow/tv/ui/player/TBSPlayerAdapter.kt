package cn.turboshow.tv.ui.player

import androidx.leanback.media.PlayerAdapter
import cn.turboshow.tv.player.TBSPlayer

class TBSPlayerAdapter(private val player: TBSPlayer) : PlayerAdapter() {
    init {
        player.setCallback(object: TBSPlayer.Callback {
            override fun onPrepared() {
                callback.onPreparedStateChanged(this@TBSPlayerAdapter)
                callback.onPlayStateChanged(this@TBSPlayerAdapter)
            }

            override fun onPositionChanged() {
                callback.onCurrentPositionChanged(this@TBSPlayerAdapter)
            }
        })
    }
    override fun pause() {
        player.pause()
    }

    override fun play() {
        player.play()
    }

    override fun seekTo(positionInMs: Long) {
        player.seekTo(positionInMs)
    }

    override fun getDuration(): Long {
        return player.getDuration()
    }

    override fun getCurrentPosition(): Long {
        return player.getPosition()
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    override fun isPrepared(): Boolean {
        return player.isPrepared()
    }
}