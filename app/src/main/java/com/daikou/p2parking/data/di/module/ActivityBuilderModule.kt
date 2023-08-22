package com.daikou.p2parking.data.di.module

import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.SimpleBaseActivity
import com.daikou.p2parking.ui.login.LoginActivity
import com.daikou.p2parking.ui.splash_screen.SplashScreenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun splashScreenActivity(): SplashScreenActivity

    @ContributesAndroidInjector
    abstract fun baseActivity(): BaseActivity

    @ContributesAndroidInjector
    abstract fun simpleBaseActivity(): SimpleBaseActivity

    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity
}