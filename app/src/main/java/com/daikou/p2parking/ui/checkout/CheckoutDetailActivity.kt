package com.daikou.p2parking.ui.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.BetterActivityResult
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityCheckoutDetailBinding
import com.daikou.p2parking.emunUtil.TicketType
import com.daikou.p2parking.helper.*
import com.daikou.p2parking.helper.HelperUtil.formatDatFromDatetime
import com.daikou.p2parking.helper.HelperUtil.formatDate
import com.daikou.p2parking.helper.extension.setBackgroundTint
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.model.User
import com.daikou.p2parking.sdk.ParkingController
import com.daikou.p2parking.ui.LotTypeActivity
import com.daikou.p2parking.ui.MainActivity
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
    private var mPaymentLink : String = ""

    private var isSuccessSever = false
    private lateinit var parkingController: ParkingController
    private lateinit var mServiceUuid : String
    private lateinit var mCharUuid : String

    private val lotTypeViewModel: LotTypeViewModel by viewModels {
        factory
    }

    companion object{
        const val TICKET_DATA_KEY = "ticket_data"
        const val PAY_BY_CASH = "by_cash"
        const val PAY_BY_ONLINE = "by_online"
        const val BY_PREVIEW = "preview"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parkingController = ParkingController()

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
//                if (mServiceUuid.isEmpty()) {
//                    isSuccessSever = true
//                } else {
//                    isSuccessSever = false
//                    parkingController.openLastParking(mServiceUuid, mCharUuid)
//                }
                MainActivity.ParkingStatus = Constants.EXIT
                connectToParking()
                MessageUtils.showSuccess(this, null, resources.getString(R.string.payment_success), { onCancelListener ->
                    onCancelListener.dismiss()
                    finish()
                }, { onSubmitPrintReceiptListener ->
                    SunmiPrintHelper.getInstance().printTicket(ticketModel, TicketType.CheckOut)
                    onSubmitPrintReceiptListener.dismiss()
                    finish()
                })
            } else {
                MessageUtils.showError(this, null, respondState.message)
                parkingController.cancelScanning()
                parkingController.disconnectFromDevice()
            }
        }
    }

    private fun initView() {
        this.setSupportActionBar(binding.appBarLayout.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.appBarLayout.title.text = getString(R.string.check_out_detail).uppercase()

        if (intent.hasExtra(TICKET_DATA_KEY) && intent.getStringExtra(TICKET_DATA_KEY) != null){
            val jsonData = intent.getStringExtra(TICKET_DATA_KEY)
            ticketModel = Config.GsonConverterHelper.getJsonObjectToGenericClass<TicketModel>(jsonData)
        }

        if (ticketModel != null) {
            HelperUtil.loadImageToImageView(this, ticketModel?.image ?: "", binding.imageCar)

            binding.ticketNo.text = ticketModel?.ticketNo ?: "N/A"

            val fromDate = if (ticketModel?.fromDate != null) formatDatFromDatetime(
                ticketModel!!.fromDate!!, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd h:mm a"
            ) else "N/A"

            binding.timeInTv.text = fromDate

            val toDate = if (ticketModel?.toDate != null) formatDatFromDatetime(
                ticketModel?.toDate!!, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd h:mm a"
            ) else "N/A"

            binding.timeOutTv.text = toDate

            binding.textAmoutTv.text = HelperUtil.formatDollaAmount(ticketModel?.totalPrice ?: 0.00)

            binding.timeUseTv.text = ticketModel?.duration ?: ". . ."


            mTicketNo = ticketModel?.ticketNo ?: ""
            mPaymentLink = ticketModel?.paymentLink ?: ""

        }
    }

    private fun initAction(){
        binding.actionPayByCaseBtn.setOnClickListener {
            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)
            MessageUtils.showConfirm(self(), getString(R.string.pay_by_case), getString(R.string.please_confirm_pay_by_case)) { sweetDialog ->
                sweetDialog.dismiss()
                submitCheckOut(PAY_BY_CASH)
            }
        }

        binding.actionOnlinePayBtn.setOnClickListener { it ->
            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)

            /*** RedirectClass.gotoDoPaymentActivity(this,
                Config.GsonConverterHelper.convertGenericClassToJson(ticketModel),
                object : BetterActivityResult.OnActivityResult<ActivityResult> {
                    override fun onActivityResult(result: ActivityResult) {
                        if (result.resultCode == RESULT_OK) {
                            MessageUtils.showSuccess(this@CheckoutDetailActivity, null, "Your payment success with cash payment.") {
                                finish()
                                it.dismiss()
                            }
                        }
                    }
                }) ***/

            if (TextUtils.isEmpty(mPaymentLink)) return@setOnClickListener

            val url = mPaymentLink // Link Url
            RedirectClass.gotoWebPay(self(), url, object : BetterActivityResult.OnActivityResult<ActivityResult>{
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data = result.data
                        if (data != null && data.hasExtra("status")){
                            val mStatus = data.getStringExtra("status")
                            if(mStatus.equals("success=1", true)) {
                                submitCheckOut(PAY_BY_ONLINE)
                            }
                        }
                    }
                }

            })
        }

//        binding.actionPayCaseBtn.setOnClickListener {
//            isPayByCash = true
//            binding.actionOnlinePayBtn.setBackgroundTint(R.color.light_gray)
//            binding.actionPayCaseBtn.setBackgroundTint(R.color.colorPrimary)
//            binding.txtOnline.setTextColor(ContextCompat.getColor(this, R.color.dark_gray))
//            binding.txtCase.setTextColor(ContextCompat.getColor(this, R.color.black))
//        }
    }

    private fun submitCheckOut(mStatus : String) {
        val requestBody = HashMap<String, Any>()
        requestBody["status"] = mStatus
        requestBody["ticket_no"] = mTicketNo
        lotTypeViewModel.submitCheckOut(requestBody)
    }

    private fun connectToParking(){
        parkingController.connectToParking(this, object : ParkingController.ParkingCallBack{
            @SuppressLint("MissingPermission")
            override fun onConnect(bluetoothDevice: BluetoothDevice) {
                parkingController.openParking(bluetoothDevice)
            }

            override fun onError(message: String?) {
                //updateUi(true, message)
            }

            //@RequiresApi(Build.VERSION_CODES.S)
            override fun onParkingOpen(message: String?, name: String?, serviceUuid: String?, charUuid: String?) {
                mServiceUuid = serviceUuid!!
                mCharUuid = charUuid!!
                parkingController.openLastParking(serviceUuid, charUuid)
                parkingController.cancelScanning()
                parkingController.disconnectFromDevice()
                if (isSuccessSever) {
                    parkingController.openLastParking(serviceUuid, charUuid)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        parkingController.cancelScanning()
        parkingController.disconnectFromDevice()
    }
}