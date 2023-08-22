package com.daikou.p2parking.data.di

import android.app.Application
import com.daikou.p2parking.EazyTaxiApplication
import com.daikou.p2parking.data.di.module.ActivityBuilderModule
import com.daikou.p2parking.data.di.module.FragmentBuilderModule
import com.daikou.p2parking.data.di.module.NetworkModule
import com.daikou.p2parking.data.di.module.ViewModelModule

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        FragmentBuilderModule::class,
        ActivityBuilderModule::class
    ]
)

interface AppComponent : AndroidInjector<EazyTaxiApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
    override fun inject(app: EazyTaxiApplication)

}


