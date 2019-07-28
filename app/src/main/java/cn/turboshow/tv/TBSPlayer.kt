package cn.turboshow.tv

import android.content.Context
import android.net.Uri
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class TBSPlayer(context: Context, private val videoView: VLCVideoLayout) {
    private val options = arrayListOf("--vout=android_display,none")
    private val libVLC = LibVLC(context, options)
    private val player = MediaPlayer(libVLC)

    fun play(uri: Uri) {
        player.attachViews(videoView, null, false, false)
        player.videoScale = MediaPlayer.ScaleType.SURFACE_16_9
        player.play(uri)
    }

    fun stop() {
        player.stop()
        player.detachViews()
    }

    fun release() {
        player.release()
        libVLC.release()
    }
}