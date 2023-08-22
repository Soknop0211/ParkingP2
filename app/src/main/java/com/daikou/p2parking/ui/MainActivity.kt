package com.daikou.p2parking.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.daikou.p2parking.R
import com.daikou.p2parking.apdapter.HomeItemAdapter
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.data.model.HomeItemModel
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityMainBinding
import com.daikou.p2parking.emunUtil.HomeScreenEnum
import com.daikou.p2parking.emunUtil.TicketType
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.PermissionRequest
import com.daikou.p2parking.helper.SunmiPrintHelper
import com.daikou.p2parking.ui.scan_check_out.ScanActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var homeItemAdapter: HomeItemAdapter
    private var imageString : String? = null
    private lateinit var ticketModel : TicketModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPrinterService()
        setupHomeItem()
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
                    PermissionRequest().cameraPermission(this){
                        if (it){
                            takePhoto()
                        }
                    }
                }
                HomeScreenEnum.ScanQR ->{
                    val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private fun initTicket(){
        ticketModel = TicketModel()
        ticketModel.image = imageString
        ticketModel.ticketNo = "T1124N0001"
        val date = Date()
        ticketModel.timeIn = HelperUtil.formatDate(date)
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

                initTicket()
                SunmiPrintHelper.getInstance().printTicket(ticketModel, TicketType.CheckIn)
//                bitmap = imageUri?.let { HelperUtil.convertBitmap(this, it) }
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