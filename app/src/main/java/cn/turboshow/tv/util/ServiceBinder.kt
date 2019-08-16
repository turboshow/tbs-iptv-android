package cn.turboshow.tv.util

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

abstract class ServiceBinder(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val unbindOn: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) {
    private val lifecycleObserver = object: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun unbindService(owner: LifecycleOwner, event: Lifecycle.Event) {
            if (event == unbindOn) {
                context.unbindService(serviceConnection)
                owner.lifecycle.removeObserver(this)
                onServiceDisconnected()
            }
        }
    }
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            onServiceDisconnected()
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }

        override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
            onServiceConnected(binder)
            lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        }
    }

    fun <T: Service>bind(cls: Class<T>) {
        context.bindService(Intent(context, cls), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    abstract fun onServiceConnected(binder: IBinder)
    abstract fun onServiceDisconnected()
}