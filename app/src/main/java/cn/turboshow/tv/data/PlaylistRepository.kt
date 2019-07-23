package cn.turboshow.tv.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import cn.turboshow.tv.model.Channel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.Okio
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi
) {
    val channels = MutableLiveData<List<Channel>>()
    val currentChannel = MutableLiveData<Channel>()

    init {
        loadChannels()
    }

    private fun loadChannels() {
        val channelListType = Types.newParameterizedType(List::class.java, Channel::class.java)
        val adapter = moshi.adapter<List<Channel>>(channelListType)
        val channelsJson = sharedPreferences.getString(KEY_CHANNELS, null)
        if (channelsJson != null) {
            channels.value = adapter.fromJson(channelsJson)
            currentChannel.value = channels.value!![currentChannelIndex]
        }
    }

    private var currentChannelIndex: Int
        get() = sharedPreferences.getInt(KEY_CURRENT_CHANNEL_INDEX, 0)
        set(value) {
            val prefEditor = sharedPreferences.edit()
            prefEditor.putInt(KEY_CURRENT_CHANNEL_INDEX, value)
            prefEditor.apply()
        }

    fun updateChannels(channels: List<Channel>) {
        if (channels.isNotEmpty()) {
            this.channels.postValue(channels)
            currentChannelIndex = 0
            this.currentChannel.postValue(channels[0])

            val channelListType = Types.newParameterizedType(List::class.java, Channel::class.java)
            val adapter = moshi.adapter<List<Channel>>(channelListType)
            val prefEditor = sharedPreferences.edit()
            prefEditor.putString(KEY_CHANNELS, adapter.toJson(channels))
            prefEditor.apply()
        }
    }

    fun selectChannel(index: Int) {
        channels.value?.let {
            currentChannel.postValue(it[index])
            currentChannelIndex = index
        }
    }

    fun nextChannel() {
        channels.value?.let {
            val currentIndex = currentChannelIndex
            val nextIndex = (currentIndex + 1) % it.size
            currentChannel.postValue(it[nextIndex])
            currentChannelIndex = nextIndex
        }
    }

    fun prevChannel() {
        channels.value?.let {
            val currentIndex = currentChannelIndex
            val nextIndex = (currentIndex + it.size - 1) % it.size
            currentChannel.postValue(it[nextIndex])
            currentChannelIndex = nextIndex
        }
    }

    companion object {
        private const val KEY_CHANNELS = "channels"
        private const val KEY_CURRENT_CHANNEL_INDEX = "current_channel_index"
    }
}