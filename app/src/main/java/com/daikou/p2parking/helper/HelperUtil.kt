package com.daikou.p2parking.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.daikou.p2parking.R
import com.daikou.p2parking.model.Constants
import com.daikou.p2parking.model.Constants.Auth.customBroadcastKey
import com.daikou.p2parking.model.LotTypeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object HelperUtil {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }

    fun hasNotNetworkAvailable(context: Context): Boolean {
        return !isNetworkAvailable(context)
    }

    fun saveLotType(context: Context, lotTypeList : ArrayList<LotTypeModel>){
        val json = Gson().toJson(lotTypeList)
        setStringSharePreference(context, Constants.LotType.LOT_TYPE_KEY, json)
    }
    fun getSaveLotType(context: Context): String {
        return getStringSharePreference(context, Constants.LotType.LOT_TYPE_KEY)
    }

    fun convertBitmap(context: Context, uri: Uri) : Bitmap{
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        return bitmap
    }

    fun generateQRCode(json: String?, width : Int, height : Int) : Bitmap? {
        val mWriter = MultiFormatWriter()
        var bitmap : Bitmap? = null
        try {
            //BitMatrix class to encode entered text and set Width & Height
            val mMatrix = mWriter.encode(json, BarcodeFormat.QR_CODE, width, height)
            val mEncoder = BarcodeEncoder()
             bitmap = mEncoder.createBitmap(mMatrix) //creating bitmap of code
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date : Date) : String {
        val fm = SimpleDateFormat("yyyy-MM-dd h:mm a")
        val printedDate = fm.format(date)
        return printedDate
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDatFromDatetime(dateTime : String, oldPatten : String, newPattern: String) : String{
        val old = SimpleDateFormat(oldPatten)
        val new = SimpleDateFormat(newPattern)
        val date = old.parse(dateTime)
        val printedDate = date?.let { new.format(it) }
        return printedDate ?: ""
    }

    fun convertToBase64(bitmap: Bitmap) : String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun convertStringToBitmap(base64String: String) : Bitmap {
        val imageByte = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
    }

    fun formatDollaAmount(amount: Double) :String{
        return String.format("%.2f $",amount )
    }

    fun loadImageToImageView(context : Context, image : Any, imageView: ImageView){
        Glide.with(context)
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.car)
            .into(imageView)
    }

    fun getStringSharePreference(context: Context, key: String) : String{
        val pref = context.getSharedPreferences(key,Context.MODE_PRIVATE)
        return pref.getString(key, "") ?: ""
    }

    fun setStringSharePreference(context: Context, key : String, value: String){
        val pref = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    //=============================================================================================
    fun changeLanguage(context: Activity, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        setStringSharePreference(context, Constants.Auth.LANGUAGE, language)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.locale = locale
        res.updateConfiguration(config, res.displayMetrics)
    }


    fun convert(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun startBroadcastData(mValue: String, mActivity : Activity) {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val intent = Intent(customBroadcastKey)
                intent.putExtra(customBroadcastKey, mValue)
                mActivity.sendBroadcast(intent)
            }, 500)
        }
    }

    object ParkingInfo{
        const val isAlreadyRequest = "isAlreadyRequest"
        fun saveParkingDevices(context: Context, devices : HashMap<String, String>){
            val gson = Gson()
            val listJson = gson.toJson(devices)
            setStringSharePreference(context,Constants.PARKING_DEVICE_LIST, listJson)
        }
        fun getParkingDevice(context: Context) : HashMap<String, String>{
            val listJson = getStringSharePreference(context, Constants.PARKING_DEVICE_LIST)
            val gson = Gson()
            val type = object : TypeToken<HashMap<String , Any>>(){}.type
            if (listJson != ""){
                return gson.fromJson(listJson, type)
            }else{
                return  HashMap()
            }
        }}

}