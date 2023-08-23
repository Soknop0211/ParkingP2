package com.daikou.p2parking.data.di.module

import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.BaseCoreActivity
import com.daikou.p2parking.base.SimpleBaseActivity
import com.daikou.p2parking.ui.LotTypeActivity
import com.daikou.p2parking.ui.MainActivity
import com.daikou.p2parking.ui.checkout.CheckoutDetailActivity
import com.daikou.p2parking.ui.checkout.DoPaymentActivity
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

    @ContributesAndroidInjector
    abstract fun baseCoreActivity(): BaseCoreActivity

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun lotTypeActivity(): LotTypeActivity

    @ContributesAndroidInjector
    abstract fun checkoutDetailActivity(): CheckoutDetailActivity

    @ContributesAndroidInjector
    abstract fun doPaymentActivity(): DoPaymentActivity
}