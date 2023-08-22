package com.daikou.p2parking

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.daikou.p2parking.data.di.DaggerAppComponent
import com.daikou.p2parking.helper.SunmiPrintHelper
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.HasAndroidInjector


open class EazyTaxiApplication :
    DaggerApplication(),
    HasAndroidInjector{

    private val applicationInjector = DaggerAppComponent.builder().application(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

    @SuppressLint("HardwareIds")
    fun deviceId(): String? {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this)
    }
}