package cn.turboshow.tv

import androidx.lifecycle.ViewModel
import cn.turboshow.tv.data.PlaylistRepository
import cn.turboshow.tv.data.SettingsRepository
import cn.turboshow.tv.service.WebServer
import fi.iki.elonen.NanoHTTPD
import javax.inject.Inject

class AppViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    settingsRepository: SettingsRepository
): ViewModel() {
    val channels = playlistRepository.channels
    val currentChannel = playlistRepository.currentChannel
    val udpxyAddr = settingsRepository.udpxyAddr

    fun selectChannel(index: Int) {
        playlistRepository.selectChannel(index)
    }

    fun nextChannel() {
        playlistRepository.nextChannel()
    }

    fun prevChannel() {
        playlistRepository.prevChannel()
    }
}