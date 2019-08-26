package cn.turboshow.iptv.tv.di

import cn.turboshow.iptv.tv.service.TBSService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBindingModule {
    @ServiceScoped
    @ContributesAndroidInjector(
        modules = [
        ]
    )
    internal abstract fun bindTBSService(): TBSService
}
