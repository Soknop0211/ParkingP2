package com.daikou.p2parking.data.di.network

import android.content.Context
import com.daikou.p2parking.EazyTaxiApplication
import com.daikou.p2parking.helper.AuthHelper
import com.daikou.p2parking.model.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*
import javax.inject.Inject


class MyServiceInterceptor @Inject constructor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionToken: String =
            AuthHelper.getAccessToken(context)
        val request = chain.request()
        val builder: Request.Builder = request.newBuilder()

        val deviceId: String? = (context as EazyTaxiApplication).deviceId()
        val fcmToken: String = AuthHelper.getAccessToken(context)

        builder.addHeader("device-token", fcmToken)
        builder.addHeader("Accept", "*/*")
        builder.addHeader("x-device-type", "mobile")
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept-Encoding", "Accept-Encoding")
        builder.addHeader("Content-Encoding", "gzip")

        builder.addHeader("processing-device-id", "K251221B00003")

        builder.addHeader("app-type", "eazy_taxi_customer")

        builder.addHeader("current-latitude", "11.561872")
        builder.addHeader("current-longitude", "104.880472")

        if (sessionToken.isNotEmpty()) {
            builder.addHeader("Authorization", "Bearer $sessionToken")
        }

        deviceId?.let { builder.addHeader("device-id", it) }

        val requestBuilder = builder.build()
        return chain.proceed(requestBuilder)
    }


}