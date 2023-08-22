package com.daikou.p2parking.utility

import android.app.Activity
import android.content.Intent
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.ui.MainActivity
import com.daikou.p2parking.ui.checkout.CheckoutDetailActivity
import com.daikou.p2parking.ui.login.LoginActivity

object RedirectClass : BaseActivity() {

    fun gotoMainActivity(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        gotoActivity(activity, intent)
    }

    fun gotoCheckoutActivity(activity: Activity, data : String){
        val intent = Intent(activity, CheckoutDetailActivity::class.java)
        intent.putExtra(CheckoutDetailActivity.TICKET_DATA_KEY, data)
        gotoActivity(activity, intent)
    }

    fun gotoLoginActivity(activity: Activity){
        val intent = Intent(activity, LoginActivity::class.java)
        gotoActivity(activity, intent)
    }
}