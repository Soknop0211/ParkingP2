package com.daikou.p2parking.ui.login

import android.os.Bundle
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.databinding.ActivityLoginBinding
import com.daikou.p2parking.helper.CustomSetOnClickViewListener
import com.daikou.p2parking.utility.RedirectClass

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        initAction()
    }

    private fun initView() {
        binding.toolbar.title.text = resources.getString(R.string.log_in)
    }

    private fun initAction() {
        binding.actionLoginMtb.setOnClickListener (CustomSetOnClickViewListener {
            RedirectClass.gotoMainActivity(this)
        })
    }
}