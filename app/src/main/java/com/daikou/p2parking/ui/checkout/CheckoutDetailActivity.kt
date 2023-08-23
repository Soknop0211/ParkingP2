package com.daikou.p2parking.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.BetterActivityResult
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityCheckoutDetailBinding
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.HelperUtil.formatDatFromDatetime
import com.daikou.p2parking.helper.HelperUtil.formatDate
import com.daikou.p2parking.helper.MessageUtils
import com.daikou.p2parking.helper.PermissionRequest
import com.daikou.p2parking.helper.extension.setBackgroundTint
import com.daikou.p2parking.ui.LotTypeActivity
import com.daikou.p2parking.utility.RedirectClass
import com.daikou.p2parking.view_model.LotTypeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class CheckoutDetailActivity : BaseActivity() {

    private lateinit var binding : ActivityCheckoutDetailBinding
    private var  ticketModel: TicketModel? = null
    private var isPayByCash = true
    private var mTicketNo : String = ""

    private val lotTypeViewModel: LotTypeViewModel by viewModels {
        factory
    }

    companion object{
        const val TICKET_DATA_KEY = "ticket_data"
        const val PAY_BY_CASH = "by_cash"
        const val PAY_BY_ONLINE = "by_online"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        initAction()

        observableField()
    }

    private fun observableField() {
        lotTypeViewModel.loadingLoginLiveData.observe(self()) {
            binding.loadingView.root.visibility = if (it) View.VISIBLE else View.GONE
        }

        lotTypeViewModel.submitCheckOutMutableLiveData.observe(this) { respondState ->
            if (respondState.success) {
                MessageUtils.showSuccess(this, null, resources.getString(R.string.payment_success) ) {
                    finish()
                    it.dismiss()
                }
            } else {
                MessageUtils.showError(this, null, respondState.message)
            }
        }

    }

    private fun initView() {
        this.setSupportActionBar(binding.appBarLayout.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.appBarLayout.title.text = getString(R.string.check_out_detail)

        if (intent.hasExtra(TICKET_DATA_KEY) && intent.getStringExtra(TICKET_DATA_KEY) != null){
            val gson = Gson()
            val jsonData = intent.getStringExtra(TICKET_DATA_KEY)
            val ticketModelTypeToken = object : TypeToken<TicketModel>(){}.type
            ticketModel = gson.fromJson(jsonData,ticketModelTypeToken )
        }

        if (ticketModel != null) {
            HelperUtil.loadImageToImageView(this, ticketModel!!.imgBase64?: "", binding.imageCar)
            binding.ticketNo.text = ticketModel?.ticketNo ?: "N/A"
            val dateStr = if (ticketModel!!.fromDate != null) formatDatFromDatetime(
                ticketModel!!.fromDate!!, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd h:mm a"
            ) else "N/A"

            binding.timeInTv.text = ticketModel?.timeIn ?: dateStr
            val date = Date()
            ticketModel!!.timeOut = formatDate(date)
            ticketModel!!.amount = 3000.0
            binding.timeOutTv.text = ticketModel?.timeOut
            binding.textAmoutTv.text = HelperUtil.formatReilAmount(ticketModel!!.amount ?: 0.00)

            mTicketNo = ticketModel?.ticketNo ?: ""
        }
    }

    private fun initAction(){
        binding.actionSubmitBtn.setOnClickListener { it ->
            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)

            submitCheckOut(PAY_BY_CASH)
        }

        binding.actionOnlinePayBtn.setOnClickListener { it ->
            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)

            RedirectClass.gotoDoPaymentActivity(this,
                object : BetterActivityResult.OnActivityResult<ActivityResult> {
                    override fun onActivityResult(result: ActivityResult) {
                        if (result.resultCode == RESULT_OK) {
                            MessageUtils.showSuccess(this@CheckoutDetailActivity, null, "Your payment success with cash payment.") {
                                finish()
                                it.dismiss()
                            }
                        }
                    }
                })

            val intent  = Intent(this@CheckoutDetailActivity, DoPaymentActivity::class.java)
            gotoActivity(this, intent)

        }

        binding.actionPayCaseBtn.setOnClickListener {
            isPayByCash = true
            binding.actionOnlinePayBtn.setBackgroundTint(R.color.light_gray)
            binding.actionPayCaseBtn.setBackgroundTint(R.color.colorPrimary)
            binding.txtOnline.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
            binding.txtCase.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun submitCheckOut(mStatus : String) {
        val requestBody = HashMap<String, Any>()
        requestBody["status"] = mStatus
        requestBody["ticket_no"] = mTicketNo
        lotTypeViewModel.submitCheckOut(requestBody)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}