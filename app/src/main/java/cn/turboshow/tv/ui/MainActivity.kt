package cn.turboshow.tv.ui

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.turboshow.tv.AppViewModel
import cn.turboshow.tv.R
import cn.turboshow.tv.data.SettingsRepository
import cn.turboshow.tv.di.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: AppViewModel
    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        setContentView(R.layout.activity_main)
        initPlayer()
        viewModel.startWebServer()
    }

    private fun initPlayer() {
        val options = arrayListOf("--vout=android_display,none")
        libVLC = LibVLC(this, options)
        mediaPlayer = MediaPlayer(libVLC)
        mediaPlayer.attachViews(videoView, null, false, false)
        mediaPlayer.videoScale = MediaPlayer.ScaleType.SURFACE_BEST_FIT

        viewModel.currentChannel.observe(this, Observer {
            play()
        })

        viewModel.udpxyAddr.observe(this, Observer {
            play()
        })
    }

    private fun play() {
        viewModel.currentChannel.value?.let {
            val url =
                if (viewModel.udpxyAddr.value != null)
                    "http://${settingsRepository.udpxyAddr.value}/rtp/${it.addr}"
                else
                    "rtp://${it.addr}"
            mediaPlayer.play(Uri.parse(url))
        }
    }

    private fun showChannelsDialog() {
        val dialog = ChannelSelectorFragment.newInstance()
        dialog.show(supportFragmentManager, "channels")
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                viewModel.nextChannel()
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                viewModel.prevChannel()
                true
            }
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                showChannelsDialog()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    override fun onDestroy() {
        viewModel.stopWebServer()
        mediaPlayer.release()
        libVLC.release()

        super.onDestroy()
    }
}
