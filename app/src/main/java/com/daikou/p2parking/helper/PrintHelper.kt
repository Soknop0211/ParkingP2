package com.daikou.p2parking.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.RemoteException
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterException
import com.sunmi.peripheral.printer.InnerPrinterManager
import com.sunmi.peripheral.printer.SunmiPrinterService

object PrintHelper {
    private var sunmiPrinterService : SunmiPrinterService? = null
    const val NoSunmiPrinter = 0x00000000
    const val CheckSunmiPrinter = 0x00000001
    const val FoundSunmiPrinter = 0x00000002
    const val LostSunmiPrinter = 0x00000003

    var sunmiPrinter : Int = CheckSunmiPrinter

    fun initPrinterService(context: Context?) {
        try {
            val ret = InnerPrinterManager.getInstance().bindService(
                context,
                innerPrinterCallback
            )
            if (!ret) {
                sunmiPrinter = NoSunmiPrinter
            }
        } catch (e: InnerPrinterException) {
            e.printStackTrace()
        }
    }

    private val innerPrinterCallback: InnerPrinterCallback = object : InnerPrinterCallback() {
        override fun onConnected(service: SunmiPrinterService) {
            sunmiPrinterService = service
            checkPrinterService(service)
        }

        override fun onDisconnected() {
            sunmiPrinterService = null
            sunmiPrinter = LostSunmiPrinter
        }
    }

    fun checkPrinterService(service: SunmiPrinterService){
        var ret = false
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service)
        }catch (e : InnerPrinterException){
            e.printStackTrace()
        }
        sunmiPrinter = if (ret) FoundSunmiPrinter else NoSunmiPrinter
    }

    fun printDoc(bitmap: Bitmap){
        if (sunmiPrinterService == null) {
            return
        }

        try {
            sunmiPrinterService!!.printBitmap(bitmap, null)
            sunmiPrinterService!!.printText("Hello Developer", null)
        }catch (e : RemoteException){
            e.printStackTrace()
        }
    }

    fun initPrinter() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return
        }
        try {
            sunmiPrinterService!!.printerInit(null)
        } catch (e: RemoteException) {
            handleRemoteException(e)
        }
    }
    private fun handleRemoteException(e: RemoteException) {
        //TODO process when get one exception
    }

}