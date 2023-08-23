package com.daikou.p2parking.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityCheckoutDetailBinding
import com.daikou.p2parking.emunUtil.TicketType
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.SunmiPrintHelper
import com.daikou.p2parking.helper.extension.setBackgroundTint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class CheckoutDetailActivity : BaseActivity() {

    private lateinit var binding : ActivityCheckoutDetailBinding
    private var  ticketModel: TicketModel? = null
    private var isPayByCash = true

    companion object{
        const val TICKET_DATA_KEY = "ticket_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPrinterService()

        initView()

        initAction()
    }

    private fun initView() {
        this.setSupportActionBar(binding.appBarLayout.toolbar)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.appBarLayout.title.text = getString(R.string.check_out_detail)

        if (intent.hasExtra(TICKET_DATA_KEY) && intent.getStringExtra(TICKET_DATA_KEY) != null){
            val gson = Gson()
            val jsonData = intent.getStringExtra(TICKET_DATA_KEY)
            val ticketModelTypeToken = object : TypeToken<TicketModel>(){}.type
            ticketModel = gson.fromJson(jsonData,ticketModelTypeToken )
        }

        if (ticketModel != null){
            HelperUtil.loadImageToImageView(this, ticketModel!!.imgBase64?: "", binding.imageCar)
            binding.ticketNo.text = ticketModel!!.ticketNo ?: "N/A"
            binding.timeInTv.text = ticketModel!!.timeIn ?: "N/A"
            val date = Date()
            ticketModel!!.timeOut = HelperUtil.formatDate(date)
            ticketModel!!.amount = 3000.0
            binding.timeOutTv.text = ticketModel!!.timeOut
            binding.textAmoutTv.text = HelperUtil.formatReilAmount(ticketModel!!.amount ?: 0.00)
        }
    }

    private fun initAction(){
        binding.actionSubmitBtn.setOnClickListener {
            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)

            if (ticketModel != null) {
                SunmiPrintHelper.getInstance().printTicket(ticketModel, TicketType.CheckOut)
                finish()
            }
        }

        binding.actionOnlinePayBtn.setOnClickListener {
            val intent  = Intent(this@CheckoutDetailActivity, DoPaymentActivity::class.java)
            gotoActivity(this, intent)

        }

        binding.actionPayCaseBtn.setOnClickListener {
            isPayByCash = true
            binding.actionOnlinePayBtn.setBackgroundTint(R.color.light_gray)
            binding.actionPayCaseBtn.setBackgroundTint(R.color.colorPrimary)
            binding.txtOnline.setTextColor(ContextCompat.getColor(this, R.color.light_gray))
            binding.txtCase.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}