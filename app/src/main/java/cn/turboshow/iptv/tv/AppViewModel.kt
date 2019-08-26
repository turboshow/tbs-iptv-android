package cn.turboshow.iptv.tv

import androidx.lifecycle.ViewModel
import cn.turboshow.iptv.tv.data.PlaylistRepository
import cn.turboshow.iptv.tv.data.SettingsRepository
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