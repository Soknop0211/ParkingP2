package com.daikou.p2parking.ui.car_photo_taking

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.daikou.p2parking.R
import com.daikou.p2parking.base.BaseActivity
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.databinding.ActivityCheckInBinding
import com.daikou.p2parking.emunUtil.TicketType
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.PermissionRequest
import com.daikou.p2parking.helper.PrintHelper
import com.daikou.p2parking.helper.SunmiPrintHelper
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*
import kotlin.math.truncate

class CheckInActivity : BaseActivity() {
    private lateinit var binding : ActivityCheckInBinding
    private var bitmap  : Bitmap? = null
    private var imageString : String? = null
    private lateinit var ticketModel: TicketModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        init()
        initView()
        initAction()
    }

    private fun initView() {
        this.setSupportActionBar(binding.appBarLayout.toolbar)
        this.supportActionBar!!.setDisplayShowTitleEnabled(false)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.appBarLayout.title.text = getText(R.string.check_in)
    }

    private fun initAction(){
        binding.containerTakePhoto.setOnClickListener {
            PermissionRequest().cameraPermission(this){
                if (it){
                    takePhoto()
                }
            }
        }
        binding.actionSubmitBtn.setOnClickListener {
            if (imageString!= null){
//               SunmiPrintHelper.getInstance().printTicket(initTicket())
                SunmiPrintHelper.getInstance().printTicket(initTicket(), TicketType.CheckIn)
            }else{
                Toast.makeText(this, "No image to print", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun takePhoto(){
        val builder: ImagePicker.Builder =
            ImagePicker.Builder(this).cameraOnly().crop()
        builder.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null) {
                val imageUri = data.data
                imageString = imageUri.toString()
                bitmap = imageUri?.let { HelperUtil.convertBitmap(this, it) }
                binding.uploadedImage.setImageURI(imageUri)
                binding.imageCarLogo.visibility = View.GONE
                binding.imageCamera.visibility = View.GONE
                binding.actionSubmitBtn.isEnabled = true
                binding.actionSubmitBtn.setStrokeColorResource(R.color.colorPrimary)
            }
        }
    }

    private fun initTicket() : TicketModel{
        ticketModel = TicketModel()
        ticketModel.imgBase64 = imageString
        ticketModel.ticketNo = "T1124N0001"
        val date = Date()
        return ticketModel
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