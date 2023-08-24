package com.daikou.p2parking.utility

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.BetterActivityResult
import com.daikou.p2parking.helper.AuthHelper.clearSession
import com.daikou.p2parking.ui.LotTypeActivity
import com.daikou.p2parking.ui.MainActivity
import com.daikou.p2parking.ui.checkout.CheckoutDetailActivity
import com.daikou.p2parking.ui.checkout.DoPaymentActivity
import com.daikou.p2parking.ui.checkout.DoPaymentActivity.Companion.TicketData
import com.daikou.p2parking.ui.login.LoginActivity

object RedirectClass : BaseActivity() {

    fun gotoMainActivity(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        gotoActivity(activity, intent)
        activity.finishAffinity()
    }

    fun gotoCheckoutActivity(activity: Activity, data : String){
        val intent = Intent(activity, CheckoutDetailActivity::class.java)
        intent.putExtra(CheckoutDetailActivity.TICKET_DATA_KEY, data)
        gotoActivity(activity, intent)
    }

    fun gotoLoginActivity(activity: Activity) {
        clearSession(activity)
        val intent = Intent(activity, LoginActivity::class.java)
        gotoActivity(activity, intent)
        activity.finishAffinity()
    }

    fun gotoLotTypeActivity(
        activity: Activity,
        jsonData: String,
        activityResult: BetterActivityResult.OnActivityResult<ActivityResult>,
        ) {
        val intent = Intent(activity, LotTypeActivity::class.java)
        intent.putExtra(LotTypeActivity.LotTypeResponse, jsonData)
        gotoActivityForResult(activity, intent, activityResult)
    }

    fun gotoDoPaymentActivity(
        activity: Activity,
        jsonData: String,
        activityResult: BetterActivityResult.OnActivityResult<ActivityResult>,
    ) {
        val intent = Intent(activity, DoPaymentActivity::class.java)
        intent.putExtra(TicketData, jsonData)
        gotoActivityForResult(activity, intent, activityResult)
    }
}