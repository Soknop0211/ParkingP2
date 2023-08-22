package com.daikou.p2parking

import android.app.Application
import com.daikou.p2parking.helper.PrintHelper
import com.daikou.p2parking.helper.SunmiPrintHelper

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this)
//        PrintHelper.initPrinterService(this)
    }
}