package com.daikou.p2parking.data.di.module

import com.daikou.p2parking.base.base.BaseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun baseFragment(): BaseFragment

}