package com.daikou.p2parking.data.di.network

import android.content.Context
import com.daikou.p2parking.EazyTaxiApplication
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.base.Config.LANGUAGE
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.model.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*
import javax.inject.Inject


class MyServiceInterceptor @Inject constructor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder: Request.Builder = request.newBuilder()

        val headerMap = HashMap<String , Any>()
        builder.addHeader("app-type", Config.APP_TYPE)
        builder.addHeader("Accept", "*/*")
        builder.addHeader("Accept", "*/*")
        builder.addHeader("x-device-type", "mobile")
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept-Encoding", "Accept-Encoding")
        builder.addHeader("Content-Encoding", "gzip")

        val deviceId: String? = (context as EazyTaxiApplication).deviceId()
        val token = AuthHelper.getAccessToken(context)
        if (token.isNotEmpty()) {
            builder.addHeader("Authorization", "Bearer $token")
        }

        when {
            deviceId != null -> {
                headerMap["device-id"] = deviceId
            }
            else -> {
                headerMap["device-id"] =  String.format("%s", Date().time)
            }
        }
        headerMap["language_code"] = LANGUAGE

        val requestBuilder = builder.build()
        return chain.proceed(requestBuilder)
    }


}