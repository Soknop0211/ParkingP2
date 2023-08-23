package com.daikou.p2parking.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity

class DoPaymentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_payment)

        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }, 5000)
    }
}