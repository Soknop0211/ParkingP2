package com.daikou.p2parking.helper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

open class PermissionRequest {
//    lateinit var onPermissionGranted : (isGranted : Boolean) -> Unit
    companion object{
        const val CAMERA_CODE = 1111
    }
    open fun cameraPermission(activity: Activity, onPermissionGranted : (isGranted : Boolean) -> Unit){
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_CODE
            )
        }else {
            onPermissionGranted.invoke(true)
        }
    }

}