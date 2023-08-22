package com.daikou.p2parking.ui.scan_check_out

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.daikou.p2parking.R
import com.daikou.p2parking.databinding.ActivityScanBinding
import com.daikou.p2parking.helper.PermissionRequest
import com.daikou.p2parking.utility.RedirectClass
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        this.setSupportActionBar(binding.appBarLayout.toolbar)
        this.supportActionBar!!.setDisplayShowTitleEnabled(false)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.appBarLayout.title.text = getText(R.string.check_out)
    }

    private fun initScanner(){
         scannerView = binding.zxingScanner
        scannerView.setAutoFocus(true)
        PermissionRequest().cameraPermission(this){hasPermission ->
            if (hasPermission){
                scannerView.setResultHandler(resultHandler)
                scannerView.startCamera()
            }
        }
    }

    private fun startCameraScan(){
//        codeScanner.setDecodeCallback { result ->
//            if (result.text.isNotEmpty()){
//                val intent = Intent(this@ScanActivity, CheckoutDetailActivity::class.java)
//                intent.putExtra(CheckoutDetailActivity.TICKET_DATA_KEY, result.text)
//                startActivity(intent)
//                finish()
//            }else{
//
//            }
//        }
    }

    private val resultHandler : ZXingScannerView.ResultHandler = ZXingScannerView.ResultHandler {
        if (it.text.isNotEmpty()){
            RedirectClass.gotoCheckoutActivity(this, it.text)
            finish()
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
        initScanner()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera() // Stop camera on pause

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
                    scannerView.startCamera()
                }else{
                    finish()  // finish activity when camera is not allowed
                }
            }
        }
    }
}
