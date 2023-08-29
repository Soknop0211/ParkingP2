package com.daikou.p2parking.model

import com.daikou.p2parking.BuildConfig

object Constants {
    object  Auth{
        const val TOKEN_KEY = "token_key"
        const val USER_KEY = "user_key"
        const val LANGUAGE = "language"
        const val customBroadcastKey: String = BuildConfig.APPLICATION_ID + ".broadcast.activity.CUSTOM_CHT_BROADCAST"
    }

    object PhoneConfig {
        const val PHONE_MIN_LENGTH = 9
        const val PHONE_MAX_LENGTH = 19 //last one 12
        const val PHONE_CONFIRMATION_CODE = 6
    }
}