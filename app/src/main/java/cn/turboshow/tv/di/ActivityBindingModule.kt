package cn.turboshow.tv.di

import cn.turboshow.tv.ui.iptv.IptvActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            BindsModule::class
        ]
    )
    internal abstract fun bindIptvActivity(): IptvActivity
}
