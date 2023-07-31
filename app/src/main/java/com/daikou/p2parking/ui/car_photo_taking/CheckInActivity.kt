package com.daikou.p2parking.ui.car_photo_taking

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.daikou.p2parking.R
import com.daikou.p2parking.databinding.ActivityCheckInBinding
import com.daikou.p2parking.helper.PermissionRequest
import com.github.dhaval2404.imagepicker.ImagePicker

class CheckInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCheckInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            PermissionRequest().CameraPermisstion(this){
                if (it){
                    takePhoto()
                }
            }
        }
    }

    private fun takePhoto(){
        val builder: ImagePicker.Builder =
            ImagePicker.Builder(this).cameraOnly().cropSquare().crop()
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
                val imageUri = data!!.data
                binding.uploadedImage.setImageURI(imageUri)
                binding.imageCarLogo.visibility = View.GONE
                binding.imageCamera.visibility = View.GONE
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