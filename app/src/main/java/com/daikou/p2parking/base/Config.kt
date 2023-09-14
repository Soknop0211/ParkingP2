package com.daikou.p2parking.base

import com.daikou.p2parking.BuildConfig
import com.google.gson.Gson

object Config {
    const val BASE_URL = BuildConfig.BASE_URL
    const val APP_TYPE = "parking_customer"
    const val LANGUAGE = "en"
    const val LANG_KH = "kh"
    const val LANG_EN = "en"
    object GsonConverterHelper {

        fun <T> convertGenericClassToJson(
            data: T,
        ): String {
            val gsonConverter = Gson()
            return gsonConverter.toJson(data)
        }

        inline fun <reified T> getJsonObjectToGenericClass(jsonData: String?): T {
            return Gson().fromJson(jsonData, T::class.java)
        }

        inline fun <reified T> getJsonObjectToGenericClassValidate(jsonData: String?): T? {
            try {
                return Gson().fromJson(jsonData, T::class.java)
            } catch (ignored: Exception) {
            }
            return null
        }

    }

}
