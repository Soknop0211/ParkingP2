package com.daikou.p2parking.ui.scan_check_out

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.daikou.p2parking.R
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class CaptureScanActivity : Activity (), DecoratedBarcodeView.TorchListener {

    private var capture: CaptureManager? = null
    private var barcodeScannerView: DecoratedBarcodeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeScannerView = initializeContent()
        capture = CaptureManager(this, barcodeScannerView)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.decode()

        initAction()

    }

    // TorchListener callback methods
    override fun onTorchOn() {
        // Flash is turned on
    }

    override fun onTorchOff() {
        // Flash is turned off
    }


    private fun initAction(){
        val imageView = findViewById<ImageView>(R.id.iconBack)
        imageView.visibility = View.VISIBLE
        imageView.setOnClickListener { finish() }

        barcodeScannerView?.setTorchListener(this)

        var isFlashOn = true
        val imgFlash = findViewById<ImageView>(R.id.btnOnOffLight)
        imgFlash.setOnClickListener {
            if (isFlashOn) {
                isFlashOn = false
                barcodeScannerView?.setTorchOn()
                imgFlash.setImageResource(R.drawable.baseline_flash_on_24)
            } else {
                isFlashOn = true
                barcodeScannerView?.setTorchOff()
                imgFlash.setImageResource(R.drawable.baseline_flash_off_24)
            }
        }
    }

    private fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.activity_capture_scan)
        return findViewById(R.id.zxing_barcode_scanner)
    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeScannerView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}