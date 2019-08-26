package cn.turboshow.iptv.tv.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScoped

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScoped

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceScoped
