package com.daikou.p2parking.ui.splash_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.SimpleBaseActivity
import com.daikou.p2parking.databinding.ActivitySplashScreenBinding
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.utility.RedirectClass

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : SimpleBaseActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAction()
    }

    private fun initAction(){
        Handler(Looper.getMainLooper()).postDelayed({
            if (AuthHelper.getAccessToken(this) != ""){
                RedirectClass.gotoMainActivity(this)
            }else{
                RedirectClass.gotoLoginActivity(this)
                finish()
            }
        }, 2000)
    }
}