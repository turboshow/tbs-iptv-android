package cn.turboshow.iptv.tv.data

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_UDPXY_ADDR = "udpxy_addr"

@Singleton
class SettingsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    val udpxyAddr = MutableLiveData<String>()

    init {
        sharedPreferences.getString(KEY_UDPXY_ADDR, null)?.let {
            udpxyAddr.value = it
        }
    }

    fun updateUdpxyAddr(addr: String?) {
        udpxyAddr.postValue(addr)
        val prefEditor = sharedPreferences.edit()
        prefEditor.putString(KEY_UDPXY_ADDR, addr)
        prefEditor.apply()
    }
}