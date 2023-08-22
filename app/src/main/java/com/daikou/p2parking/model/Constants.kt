package com.daikou.p2parking.model

object Constants {
    object  Auth{
        const val TOKEN_KEY = "token_key"
        const val USER_KEY = "user_key"
        const val PASSWORD_KEY = "password_key"
    }

    object PhoneConfig {
        const val PHONE_MIN_LENGTH = 9
        const val PHONE_MAX_LENGTH = 19 //last one 12
        const val PHONE_CONFIRMATION_CODE = 6
    }
}