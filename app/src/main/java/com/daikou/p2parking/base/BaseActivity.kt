package com.daikou.p2parking.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.helper.SunmiPrintHelper
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseActivity : BaseCoreActivity() {

    private lateinit var handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
        initPrinter()
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