package com.daikou.p2parking.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.daikou.p2parking.R
import com.daikou.p2parking.apdapter.HomeItemAdapter
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.BetterActivityResult
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.data.model.HomeItemModel
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityMainBinding
import com.daikou.p2parking.emunUtil.HomeScreenEnum
import com.daikou.p2parking.emunUtil.TicketType
import com.daikou.p2parking.helper.*
import com.daikou.p2parking.model.LotTypeModel
import com.daikou.p2parking.ui.checkout.CheckoutDetailActivity
import com.daikou.p2parking.ui.scan_check_out.CaptureScanActivity
import com.daikou.p2parking.utility.RedirectClass
import com.daikou.p2parking.view_model.LotTypeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var homeItemAdapter: HomeItemAdapter
    private var imgBase64 : String? = null
    private var backUpLotTypeData = ""

    private var mLotTypeModel: LotTypeModel?= null

    private val lotTypeViewModel: LotTypeViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPrinterService()

        setupHomeItem()

        observableField()
    }

    private fun observableField() {

        lotTypeViewModel.loadingLoginLiveData.observe(self()) {
            binding.loadingView.root.visibility = if (it) View.VISIBLE else View.GONE
        }


        // Lot Type
        lotTypeViewModel.dataListAllLotLiveDataLiveData.observe(self()) {
            if (it != null && it.success) {
                if (it.data != null && it.data.isNotEmpty()) {
                    backUpLotTypeData = Config.GsonConverterHelper.convertGenericClassToJson(it.data)
                    gotoLotTypeScreen(backUpLotTypeData)
                }
            } else {
                MessageUtils.showError(this, null, it.message)
            }
        }

        // Do Check In
        lotTypeViewModel.submitCheckInMutableLiveData.observe(self()) {
            if (it != null && it.success) {
                if (it.data != null) {
                    it.data.image = imgBase64

                    SunmiPrintHelper.getInstance().printTicket(it.data, TicketType.CheckIn)
                }
            } else {
                MessageUtils.showError(this, null, it.message)
            }
        }

        // Preview
        lotTypeViewModel.submitCheckOutMutableLiveData.observe(this) { respondState ->
            if (respondState.success) {
                RedirectClass.gotoCheckoutActivity(this, Config.GsonConverterHelper.convertGenericClassToJson(respondState.data))
            } else {
                MessageUtils.showError(this, null, respondState.message)
            }
        }

        binding.btnLogout.setOnClickListener { it ->
            it.isEnabled = false
            it.postDelayed({ it.isEnabled = true }, 500)

            MessageUtils.showConfirm(
                self(),
                "",
                getString(R.string.confirm_to_sign_out)
            ) {
                it.dismiss()
                RedirectClass.gotoLoginActivity(this)
            }
        }
    }

    private fun gotoLotTypeScreen(jsonData: String,) {
        RedirectClass.gotoLotTypeActivity(this, jsonData,
            object : BetterActivityResult.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == RESULT_OK) {
                        val intent = result.data
                        if (intent != null) {
                            val mLotTypeModelString =
                                intent.getStringExtra(LotTypeActivity.LotTypeResponse)
                            mLotTypeModel =
                                Config.GsonConverterHelper.getJsonObjectToGenericClass(
                                    mLotTypeModelString
                                )

                            PermissionRequest().cameraPermission(this@MainActivity){ itt ->
                                if (itt){
                                    takePhoto()
                                }
                            }
                        }

                    }
                }
            })
    }

    private fun setupHomeItem() {
        val itemList  = ArrayList<HomeItemModel>()
        itemList.add(HomeItemModel(R.drawable.camera, resources.getString(R.string.check_in), HomeScreenEnum.TakePhoto))
        itemList.add(HomeItemModel(R.drawable.qr_code_symbol, resources.getString(R.string.check_out), HomeScreenEnum.ScanQR))
        homeItemAdapter = HomeItemAdapter()
        homeItemAdapter.setRow(itemList)
        binding.recyclerViewHome.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewHome.adapter = homeItemAdapter
        homeItemAdapter.selectedItem = { homeItem ->
            when (homeItem.action ){
                HomeScreenEnum.TakePhoto -> {
                    if (!TextUtils.isEmpty(backUpLotTypeData)) {
                        gotoLotTypeScreen(backUpLotTypeData)
                    } else {
                        lotTypeViewModel.fetchLotType()
                    }
                }
                HomeScreenEnum.ScanQR ->{
                    // val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    // startActivity(intent)
                    val options = ScanOptions()
                    options.setPrompt("Scan QR Code")
                    options.setCameraId(0) // Use a specific camera of the device

                    options.setBeepEnabled(true)
                    options.setOrientationLocked(true)
                    options.captureActivity = CaptureScanActivity::class.java
                    barcodeLauncher.launch(options)
                }
            }
        }
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                AppLOGG.d("Cancelled", "Cancelled Scan")
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                MessageUtils.showError(this, null, "Cancelled due to missing camera permission.")

            }
        } else {
            submitCheckOut(result.contents)
        }
    }

    private var  ticketModel: TicketModel? = null

    private fun submitCheckOut(jsonData : String) {
        ticketModel = Config.GsonConverterHelper.getJsonObjectToGenericClass<TicketModel>(jsonData)

        val requestBody = java.util.HashMap<String, Any>()
        requestBody["status"] = CheckoutDetailActivity.BY_PREVIEW
        requestBody["ticket_no"] = ticketModel?.ticketNo ?: ""
        lotTypeViewModel.submitCheckOut(requestBody)
    }

    private fun takePhoto(){
        val builder: ImagePicker.Builder =
            ImagePicker.Builder(this).cameraOnly()
        builder.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null) {
                val imageUri = data.data

                if (imageUri != null) {
                    imgBase64 = imageUri.toString()
                }

                var bitmap = imageUri?.let { HelperUtil.convertBitmap(this, it) }
                bitmap = bitmap?.let { HelperUtil.getResizedBitmap(it, 700) }

                if (bitmap != null && mLotTypeModel != null) {
                    val pathImage: String? = HelperUtil.convert(bitmap)

                    val requestBody = HashMap<String, Any>()
                    requestBody["lot_type_id"] = mLotTypeModel?.id ?: ""
                    if (pathImage != null) {
                        requestBody["image"] = String.format(
                            "data:image/jpeg;base64,%s",
                            pathImage
                        )
                    }

                    lotTypeViewModel.submitChecking(requestBody)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionRequest.CAMERA_CODE){
            if (grantResults.isNotEmpty()){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto()
                }
            }
        }
    }
}