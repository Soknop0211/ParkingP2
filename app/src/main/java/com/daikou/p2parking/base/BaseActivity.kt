package com.daikou.p2parking.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResult
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.SunmiPrintHelper
import com.daikou.p2parking.model.Constants

open class BaseActivity : BaseCoreActivity() {

    private lateinit var handler : Handler
    var mLanguage : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        initPrinter()

        mLanguage= HelperUtil.getStringSharePreference(this, Constants.Auth.LANGUAGE)
        if (mLanguage == "") {
            mLanguage = "en"
        }
        HelperUtil.changeLanguage(this, mLanguage ?: "en")
    }

    private fun initPrinter(){
        SunmiPrintHelper.getInstance().initPrinter()
    }

    open fun gotoActivity(activity : Activity, intent : Intent){
        activity.startActivity(intent)
    }

    open fun gotoActivityForResult(activity: Activity,intent: Intent, activityResult: BetterActivityResult.OnActivityResult<ActivityResult>){
        if(activity is BaseActivity) {
            activity.activityLauncher.launch(intent, activityResult)
        }
    }

    open fun initPrinterService(){
        when (SunmiPrintHelper.getInstance().sunmiPrinter) {
            SunmiPrintHelper.NoSunmiPrinter -> {
                Log.d("no printer", "No printer")
            }
            SunmiPrintHelper.CheckSunmiPrinter -> {
                handler.postDelayed(
                    {
                        initPrinterService()
                    }, 2000)
            }
            SunmiPrintHelper.FoundSunmiPrinter -> {
                Log.d("connecting", "Connecting to printer")
            }
            else -> {
                SunmiPrintHelper.getInstance().initSunmiPrinterService(this)
            }
        }
    }

    open fun isLogin(mActivity : Activity) : Boolean {
        return AuthHelper.getAccessToken(mActivity) != ""
    }

}