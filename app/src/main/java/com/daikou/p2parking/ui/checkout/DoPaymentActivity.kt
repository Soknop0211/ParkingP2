package com.daikou.p2parking.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityDoPaymentBinding
import com.daikou.p2parking.helper.HelperUtil

class DoPaymentActivity : BaseActivity() {

    private lateinit var binding : ActivityDoPaymentBinding

    companion object {
        const val TicketData = "TicketData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }, 6 * 5000)


        if (intent.hasExtra(TicketData) && intent.getStringExtra(TicketData) != null){
            val jsonData = intent.getStringExtra(TicketData)
            val ticketModel = Config.GsonConverterHelper.getJsonObjectToGenericClass<TicketModel>(jsonData)

            binding.amountTv.text = HelperUtil.formatDollaAmount(ticketModel.totalPrice ?: 0.00)
        }

    }
}