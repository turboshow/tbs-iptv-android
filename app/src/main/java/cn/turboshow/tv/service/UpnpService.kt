package cn.turboshow.tv.service

import io.reactivex.subjects.BehaviorSubject
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.registry.RegistryListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpnpService @Inject constructor() {
    private var upnpService: org.fourthline.cling.UpnpService? = null

    val devicesSubject = BehaviorSubject.createDefault<List<RemoteDevice>>(listOf())

    fun start() {
        upnpService = UpnpServiceImpl(object : RegistryListener {
            override fun localDeviceRemoved(registry: Registry?, device: LocalDevice?) {
            }

            override fun remoteDeviceDiscoveryStarted(registry: Registry?, device: RemoteDevice?) {
            }

            override fun remoteDeviceDiscoveryFailed(registry: Registry?, device: RemoteDevice?, ex: Exception?) {
            }

            override fun afterShutdown() {
            }

            override fun remoteDeviceAdded(registry: Registry?, device: RemoteDevice?) {
                device?.let {
                    devicesSubject.onNext(devicesSubject.value!!.plus(it))
                }
            }

            override fun remoteDeviceUpdated(registry: Registry?, device: RemoteDevice?) {
            }

            override fun beforeShutdown(registry: Registry?) {
            }

            override fun remoteDeviceRemoved(registry: Registry?, device: RemoteDevice?) {
                device?.let {
                    devicesSubject.onNext(devicesSubject.value!!.minus(it))
                }
            }

            override fun localDeviceAdded(registry: Registry?, device: LocalDevice?) {
            }
        })
    }

    fun stop() {
        upnpService?.let {
            it.shutdown()
            upnpService = null
            devicesSubject.onNext(listOf())
        }
    }
}