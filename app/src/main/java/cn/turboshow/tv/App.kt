package cn.turboshow.tv

import cn.turboshow.tv.di.DaggerAppComponent
import cn.turboshow.tv.service.TBSService
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