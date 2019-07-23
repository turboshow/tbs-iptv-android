package cn.turboshow.tv.service

import android.content.Context
import cn.turboshow.tv.data.PlaylistRepository
import cn.turboshow.tv.data.SettingsRepository
import cn.turboshow.tv.model.Channel
import cn.turboshow.tv.model.UdpxySettings
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import fi.iki.elonen.NanoHTTPD
import okio.Okio
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebServer @Inject constructor(
    private val context: Context,
    private val moshi: Moshi,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository
) : NanoHTTPD(1212) {
    private val wwwRoot = "www"

    private fun get(session: IHTTPSession, pathPattern: String): Boolean {
        return session.method == Method.GET && session.uri == pathPattern
    }

    private fun post(session: IHTTPSession, pathPattern: String): Boolean {
        return session.method == Method.POST && session.uri == pathPattern
    }

    private fun getPlaylist(session: IHTTPSession): Response {
        return newFixedLengthResponse("playlist")
    }

    private fun updatePlaylist(session: IHTTPSession): Response {
        val files = mutableMapOf<String, String>()
        session.parseBody(files)
        val tmp = File(files["playlist"])
        val channelListType = Types.newParameterizedType(List::class.java, Channel::class.java)
        val adapter = moshi.adapter<List<Channel>>(channelListType)
        val playlist = adapter.fromJson(Okio.buffer(Okio.source(tmp.inputStream())))
        playlistRepository.updateChannels(playlist!!)

        return newFixedLengthResponse("accepted")
    }

    private fun getUdpxySettings(session: IHTTPSession): Response {
        val adapter = moshi.adapter(UdpxySettings::class.java)
        return newFixedLengthResponse(adapter.toJson(UdpxySettings(settingsRepository.udpxyAddr.value)))
    }

    private fun updateUdpxySettings(session: IHTTPSession): Response {
        val adapter = moshi.adapter(UdpxySettings::class.java)
        val settings = adapter.fromJson(Okio.buffer(Okio.source(session.inputStream)))
        settingsRepository.updateUdpxyAddr(settings!!.addr)

        return newFixedLengthResponse("accepted")
    }

    private fun serveFiles(session: IHTTPSession): Response {
        var targetPath = "$wwwRoot${session.uri}".dropLastWhile { it == '/' }
        val assets = context.assets
        if (assets.list(targetPath)?.isNotEmpty() == true) {
            targetPath = "$targetPath/index.html"
        }

        val targetFd = try {
            assets.openFd(targetPath)
        } catch (e: Exception) {
            assets.openFd("$wwwRoot/index.html")
        }

        return newFixedLengthResponse(
            Response.Status.OK,
            null,
            targetFd.createInputStream(),
            targetFd.length
        )
    }

    private fun handleRequests(session: IHTTPSession): Response {
        return when {
            get(session, "/api/settings/playlist") -> {
                getPlaylist(session)
            }
            post(session, "/api/settings/playlist") -> {
                updatePlaylist(session)
            }
            get(session, "/api/settings/udpxy") -> {
                getUdpxySettings(session)
            }
            post(session, "/api/settings/udpxy") -> {
                updateUdpxySettings(session)
            }
            else -> {
                serveFiles(session)
            }
        }
    }

    override fun serve(session: IHTTPSession): Response {
        return handleRequests(session)
    }
}