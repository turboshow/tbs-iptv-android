package cn.turboshow.tv.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import cn.turboshow.tv.App
import cn.turboshow.tv.AppViewModel
import cn.turboshow.tv.ui.iptv.ChannelSelectorFragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }
}

@Module
internal abstract class BindsModule {
    @Binds
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    abstract fun bindAppViewModel(viewModel: AppViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun contributeChannelSelectorFragment(): ChannelSelectorFragment
}
