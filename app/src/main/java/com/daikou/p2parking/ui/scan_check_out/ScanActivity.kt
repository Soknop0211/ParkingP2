package com.daikou.p2parking.ui.scan_check_out

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.daikou.p2parking.R
import com.daikou.p2parking.databinding.ActivityScanBinding
import com.daikou.p2parking.helper.PermissionRequest

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initScanner()
    }

    private fun initView() {
        this.setSupportActionBar(binding.appBarLayout.toolbar)
        this.supportActionBar!!.setDisplayShowTitleEnabled(false)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.appBarLayout.title.text = getText(R.string.check_out)
    }

    private fun initScanner(){
        val scannerView = binding.codeScannerView
        codeScanner = CodeScanner(this, scannerView)

        PermissionRequest().CameraPermisstion(this){hasPermission ->
            if (hasPermission){
                startCameraScan()
            }
        }
    }

    private fun startCameraScan(){
        codeScanner.setDecodeCallback { result ->
            if (result.text.isNotEmpty()){
//                Toast.makeText(this, "error scanning", Toast.LENGTH_SHORT).show()
                finish()
            }else{

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
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
                    startCameraScan()
                }
            }
        }
    }
}
