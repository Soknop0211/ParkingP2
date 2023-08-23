package com.daikou.p2parking.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.MessageUtils
import com.daikou.p2parking.helper.PermissionRequest
import com.daikou.p2parking.helper.SunmiPrintHelper
import com.daikou.p2parking.model.LotTypeModel
import com.daikou.p2parking.ui.scan_check_out.ScanActivity
import com.daikou.p2parking.utility.RedirectClass
import com.daikou.p2parking.view_model.LotTypeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var homeItemAdapter: HomeItemAdapter
    private var imageString : String? = null
    private lateinit var ticketModel : TicketModel

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
                    RedirectClass.gotoLotTypeActivity(this, Config.GsonConverterHelper.convertGenericClassToJson(it.data),
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
            } else {
                MessageUtils.showError(this, null, it.message)
            }
        }

        // Do Check In
        lotTypeViewModel.submitCheckInMutableLiveData.observe(self()) {
            if (it != null && it.success) {

                ticketModel = TicketModel()
                ticketModel.image = imageString
                ticketModel.ticketNo = "T1124N0001"
                val date = Date()
                ticketModel.timeIn = HelperUtil.formatDate(date)

                SunmiPrintHelper.getInstance().printTicket(ticketModel, TicketType.CheckIn)
            } else {
                MessageUtils.showError(this, null, it.message)
            }
        }
    }

    private fun setupHomeItem() {
        val itemList  = ArrayList<HomeItemModel>()
        itemList.add(HomeItemModel(R.drawable.car, resources.getString(R.string.check_in), HomeScreenEnum.TakePhoto))
        itemList.add(HomeItemModel(R.drawable.qr_code_symbol,resources.getString(R.string.check_out), HomeScreenEnum.ScanQR))
        homeItemAdapter = HomeItemAdapter()
        homeItemAdapter.setRow(itemList)
        binding.recyclerViewHome.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewHome.adapter = homeItemAdapter
        homeItemAdapter.selectedItem = { homeItem ->
            when (homeItem.action ){
                HomeScreenEnum.TakePhoto -> {
                    lotTypeViewModel.fetchLotType()
                }
                HomeScreenEnum.ScanQR ->{
                    val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    startActivity(intent)
                }
            }
        }
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
                //                val bitmap = imageUri?.let { HelperUtil.convertBitmap(this, it) }
                //                imageString = bitmap?.let { HelperUtil.convertToBase64(it) }
                imageString = imageUri.toString()

                var bitmap = imageUri?.let { HelperUtil.convertBitmap(this, it) }
                bitmap = bitmap?.let { HelperUtil.getResizedBitmap(it, 700) }

                if (bitmap != null && mLotTypeModel != null) {
                    val pathImage: String? = HelperUtil.convert(bitmap)

                    val requestBody = HashMap<String, Any>()
                    requestBody["lot_type_id"] = mLotTypeModel?.id ?: ""
                    requestBody["image"] = pathImage ?: ""

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