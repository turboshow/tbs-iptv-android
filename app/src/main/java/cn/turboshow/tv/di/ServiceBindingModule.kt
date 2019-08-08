package cn.turboshow.tv.di

import cn.turboshow.tv.service.TBSService
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
