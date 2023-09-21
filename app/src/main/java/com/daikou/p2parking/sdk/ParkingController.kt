package com.daikou.p2parking.sdk

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.daikou.p2parking.helper.HelperUtil
import com.daikou.p2parking.helper.ParkingId
import com.daikou.p2parking.sdk.Util.hexStringToByteArray
import com.daikou.p2parking.ui.MainActivity
import java.util.Collections
import java.util.UUID


class ParkingController {

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var bluetoothGatt : BluetoothGatt? = null

    private var activity : Activity ?= null
    private var callBack : ParkingCallBack?= null
    private var address : String = ""
    private var connectName = ""
    private var listDevice = ArrayList<Int>()
    private var isSeeResultScan = false

    var isConnect = false
    private var isCancel = false

    companion object {
        private const val REQUEST_ENABLE_BLUETOOTH = 3
        private const val PERMISSION_REQUEST_BLUETOOTH_CONNECT = 2
        private const val PERMISSION_REQUEST_CODE = 123
    }

    //@RequiresApi(Build.VERSION_CODES.P)
    fun connectToParking(activity: Activity,listener: ParkingCallBack) {
        this.activity = activity
        this.callBack = listener

        stopScanning()
        disconnectFromDevice()
        connect()
    }

    //@RequiresApi(Build.VERSION_CODES.P)
    fun connectToParking(activity: Activity, address : String,listener: ParkingCallBack) {
        this.activity = activity
        this.callBack = listener
        this.address = address

        stopScanning()
        disconnectFromDevice()
        connect()
    }

    private fun connect () {
        isCancel = false
        isSeeResultScan = false
        bluetoothManager = activity!!.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager!!.adapter
        bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner
        // Check if Bluetooth is supported on the device
        if (!activity?.packageManager!!.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            callBack!!.onError("Bluetooth is not supported on this device")
            return
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!bluetoothAdapter!!.isEnabled) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (activity?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.BLUETOOTH_CONNECT) } != PackageManager.PERMISSION_GRANTED) {
                    // Request the Bluetooth connect permission if it is not granted
                    activity?.let {
                        ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                            PERMISSION_REQUEST_BLUETOOTH_CONNECT
                        )
                    }
                    return
                }
                activity!!.startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            }
        }else{
            if (!bluetoothAdapter!!.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                activity!!.startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH)
            }
        }

        val serviceUuid = ParcelUuid.fromString("0000FFF0-0000-1000-8000-00805F9B34FB")

        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(serviceUuid)
            .build()

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
            val bluetoothScanPermission = Manifest.permission.BLUETOOTH_SCAN
            val permissions = arrayOf(fineLocationPermission, bluetoothScanPermission)
            val grantedPermissions = permissions.filter { ActivityCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_GRANTED }
            if (grantedPermissions.size != permissions.size) {
                ActivityCompat.requestPermissions(activity!!, permissions, PERMISSION_REQUEST_CODE)
                return
            }
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            bluetoothLeScanner!!.startScan(null, scanSettings, leScanCallback)
        } else {
            bluetoothLeScanner!!.startScan(null, scanSettings,leScanCallback)
        }

    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            isSeeResultScan = true
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    PERMISSION_REQUEST_BLUETOOTH_CONNECT
                )
            }
            val device = result.device
            Log.d("asdfadsfasdfadsf","Name : " +device.name + " rssi : " + result.rssi + "sum : " + listDevice.sum() + "uuid :" + result.device.uuids)

            if (address.isNotEmpty()) {
                if (device.name != null && device.name.contains(address)) {
                    if(!isConnect){
                        isConnect = true
                        connectName = device.name
                        connectToDeviceSelected(result.device)
                    }
                }
            } else {
                val deviceName = ParkingId.PARKING_ID[device.name]

                if (MainActivity.ParkingStatus == deviceName) {
                    listDevice.add(result.rssi)
                    if (listDevice.size == 1) {
                        connectName = device.name
                        if (listDevice.sum() >= -100) {
                            callBack?.onConnect(result.device)
                        } else {
                            callBack?.onError("Parking is too far, please come closer.")
                            stopScanning()
                        }
                    }
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            when (errorCode) {
                1 -> {
                    callBack?.onError("SCAN FAILED ALREADY STARTED")
                }
                2 -> {
                    callBack?.onError("SCAN FAILED APPLICATION REGISTRATION FAILED")
                }
                3 -> {
                    callBack?.onError("SCAN FAILED INTERNAL ERROR")
                }
                4 -> {
                    callBack?.onError("SCAN FAILED FEATURE UNSUPPORTED")
                }
                5 -> {
                    callBack?.onError("SCAN FAILED OUT OF HARDWARE RESOURCES")
                }
                6 -> {
                    callBack?.onError("SCAN FAILED SCANNING TOO FREQUENTLY")
                }
            }
        }
    }

    private fun connectToDeviceSelected(bluetoothDevice: BluetoothDevice) {
        callBack?.onConnect(bluetoothDevice)
    }

    fun openParking (bluetoothDevice: BluetoothDevice) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
        bluetoothGatt = bluetoothDevice.connectGatt(activity!!, true, btleGattCallback,BluetoothDevice.TRANSPORT_LE)
    }

    fun openLastParking (serviceUuid : String? , charUuid : String?) {
        openParking(serviceUuid!!,charUuid!!)
    }

    private val btleGattCallback = object : BluetoothGattCallback() {
        //@RequiresApi(Build.VERSION_CODES.P)
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            val gattServices = gatt?.services ?: return
            // Loops through available GATT Services.
            for (gattService in gattServices) {
                val serviceUuid = gattService.uuid.toString()
                ArrayList<HashMap<String, String>>()
                val gattCharacteristics = gattService.characteristics
                // Loops through available Characteristics.
                var charUuid = ""
                for (gattCharacteristic in gattCharacteristics) {
                    charUuid = gattCharacteristic.uuid.toString()
                }

                activity!!.runOnUiThread {
                    callBack!!.onParkingOpen("Parking is open" , connectName , serviceUuid , charUuid)
                }
                break
            }
        }

        //@RequiresApi(Build.VERSION_CODES.S)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothGatt.STATE_CONNECTED -> {
                    // Discover services and characteristics for this device
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            return
                        }
                    }
                    gatt.discoverServices()
                }
                BluetoothGatt.STATE_DISCONNECTED -> {
                    callBack!!.onError("Disconnected")
                }
                else -> {
                    callBack!!.onError("we encountered an unknown state, uh oh")
                }
            }
        }

    }

    //@RequiresApi(Build.VERSION_CODES.S)
    private fun openParking(serviceUuid : String, characteristicUuid : String) {

        if (bluetoothGatt == null) {
            callBack!!.onError("BluetoothGatt is null. GATT connection not established.")
            return
        }
        val service = bluetoothGatt!!.getService(UUID.fromString(serviceUuid))
        if (service == null) {
            callBack!!.onError("Gatt Service not found.")
            return
        }
        val characteristic = service.getCharacteristic(UUID.fromString(characteristicUuid))
        if (characteristic == null) {
            callBack!!.onError("Gatt Characteristic not found.")
            return
        }

        //AB550172010000000000AF open
        val data: ByteArray = hexStringToByteArray("AB550172010000000000AF")

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
        characteristic.value = data
        bluetoothGatt!!.writeCharacteristic(characteristic)
        disconnectFromDevice()
    }

    interface ParkingCallBack {
        fun onParkingOpen(message: String?, name : String?,serviceUuid : String? , charUuid : String?)
        fun onConnect (bluetoothDevice: BluetoothDevice)
        fun onError(message: String?)
    }

    fun cancelScanning() {
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val bluetoothScanPermission = Manifest.permission.BLUETOOTH_SCAN
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        val permissions = arrayOf(fineLocationPermission, bluetoothScanPermission,coarseLocationPermission)
        val grantedPermissions = permissions.filter {
            ActivityCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_GRANTED
        }

        if (grantedPermissions.size != permissions.size) {
            ActivityCompat.requestPermissions(activity!!, permissions, PERMISSION_REQUEST_CODE)
            return
        }
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner!!.stopScan(leScanCallback)
        }
    }

    //@RequiresApi(Build.VERSION_CODES.S)
    private fun stopScanning() {
        isCancel = true
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val bluetoothScanPermission = Manifest.permission.BLUETOOTH_SCAN
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        val permissions = arrayOf(fineLocationPermission, bluetoothScanPermission,coarseLocationPermission)
        val grantedPermissions = permissions.filter {
            ActivityCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_GRANTED
        }

        if (grantedPermissions.size != permissions.size) {
            ActivityCompat.requestPermissions(activity!!, permissions, PERMISSION_REQUEST_CODE)
            return
        }
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner!!.stopScan(leScanCallback)
        }
    }

    //@RequiresApi(Build.VERSION_CODES.S)
    fun disconnectFromDevice() {
        isCancel = true
        isConnect = false
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
        if (bluetoothGatt != null) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
        }
    }

}