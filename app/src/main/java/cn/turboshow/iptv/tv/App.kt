package cn.turboshow.iptv.tv

import cn.turboshow.iptv.tv.di.DaggerAppComponent
import cn.turboshow.iptv.tv.service.TBSService
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        startService(TBSService.newIntent(this))
    }
}