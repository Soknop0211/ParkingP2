package com.daikou.p2parking.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityLotTypeBinding
import com.daikou.p2parking.emunUtil.TicketType
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.PermissionRequest
import com.daikou.p2parking.helper.SunmiPrintHelper
import com.daikou.p2parking.model.LotTypeModel
import com.daikou.p2parking.ui.adapter.LotTypeAdapter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.*

class LotTypeActivity : BaseActivity() {

    private lateinit var binding : ActivityLotTypeBinding
    private var mList = ArrayList<LotTypeModel>()
    private lateinit var ticketModel : TicketModel
    private var imageString : String? = null

    companion object {
        const val LotTypeResponse = "LotTypeResponse"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLotTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getString(R.string.lot_type)

        // Init View
        binding.toolbar.title.text = resources.getString(R.string.log_in)
        binding.toolbar.iconBack.visibility = View.VISIBLE
        binding.toolbar.iconBack.setOnClickListener { finish() }

        if (intent != null && intent.hasExtra(LotTypeResponse)) {
            val jsonDataListBank = intent.getStringExtra(LotTypeResponse)
            val gson = Gson()
            val bankAccountTypeToken =
                object : TypeToken<ArrayList<LotTypeModel>>() {}.type
            mList = gson.fromJson(jsonDataListBank, bankAccountTypeToken)
        }

        val mLotTypeAdapter = LotTypeAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(self())
            adapter = mLotTypeAdapter
        }
        mLotTypeAdapter.addHomeScreen(mList)

        mLotTypeAdapter.selectedRow = {
            val intent = Intent()
            intent.putExtra(LotTypeResponse, Config.GsonConverterHelper.convertGenericClassToJson(it))
            setResult(RESULT_OK, intent)
            finish()
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
        val builder: ImagePicker.Builder = ImagePicker.Builder(this).cameraOnly()
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

                var bitmap = imageUri?.let { HelperUtil.convertBitmap(this, it) }
                bitmap = bitmap?.let { HelperUtil.getResizedBitmap(it, 700) }

                if (bitmap != null) {
                    val pathImage: String? = HelperUtil.convert(bitmap)
                    SunmiPrintHelper.getInstance().printTicket(ticketModel, TicketType.CheckIn)
                    finish()
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