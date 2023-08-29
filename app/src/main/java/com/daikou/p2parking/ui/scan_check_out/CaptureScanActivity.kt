package com.daikou.p2parking.ui.scan_check_out

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.daikou.p2parking.R
import com.daikou.p2parking.base.base.BaseEdittextListener
import com.daikou.p2parking.databinding.FragmentSearchTicketBinding
import com.daikou.p2parking.helper.CustomSetOnClickViewListener
import com.daikou.p2parking.helper.HelperUtil
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

        val iconSearch = findViewById<ImageView>(R.id.iconSearch)
        imageView.visibility = View.GONE
        iconSearch.setOnClickListener (CustomSetOnClickViewListener {
            capture!!.onPause()
            showDialog(this)
        })

    }

    private fun showDialog(context : Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        val inflater = LayoutInflater.from(context)
        val binding = FragmentSearchTicketBinding.inflate(inflater)
        builder.setView(binding.root)

        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)

        binding.txtCancel.setOnClickListener {
            capture!!.onResume()
            dialog.dismiss()
        }
        binding.txtOk.setOnClickListener {
            HelperUtil.startBroadcastData(binding.ticketNoEdt.text.toString(), this)
        }

        binding.ticketNoEdt.addTextChangedListener(object : BaseEdittextListener() {
            override fun afterTextChangedNotEmpty(editable: Editable) {
                binding.txtOk.isEnabled = true
                binding.txtOk.backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorPrimary)
            }

            override fun afterTextChangedIsEmpty() {
                binding.txtOk.isEnabled = false
                binding.txtOk.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_gray)
            }

        })

        dialog.show()
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