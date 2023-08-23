package com.daikou.p2parking.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.daikou.p2parking.helper.HelperUtil.getStringSharePreference
import com.daikou.p2parking.helper.HelperUtil.setStringSharePreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

object HelperUtil {

    fun convertBitmap(context: Context, uri: Uri) : Bitmap{
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        return bitmap
    }

    fun generateQRCode(json: String?, width : Int, height : Int) : Bitmap?{
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
        return bitmap
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

    fun convertStringToBitmap(imageString: String) : Bitmap{
        val imageByte = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
    }

    fun formatReilAmount(amount: Double) :String{
        return String.format("%.2f Riels",amount )
    }

    fun loadImageToImageView(context : Context, image : Any, imageView: ImageView){
        Glide.with(context)
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
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

}