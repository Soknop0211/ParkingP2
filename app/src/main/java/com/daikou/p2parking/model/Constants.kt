package com.daikou.p2parking.model

import com.daikou.p2parking.BuildConfig

object Constants {

    object LotType{
        const val LOT_TYPE_KEY = "lot_type_key"
    }
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

    object TypeDeepLink {
        const val kessChatDeepLink = "io.kessinnovation.kesschat"
        const val acledaDeepLink = "com.domain.acledabankqr"
        const val sathapanaDeepLink = "kh.com.sathapana.consumer"
        const val abaDeepLink = "com.paygo24.ibank"
        const val kess_url = "https://kesspay.io"
    }

    object Config {
        const val APP_TYPE = "parking_customer"
        const val BASE_URL_TEST = "https://jsonplaceholder.typicode.com/"

        object TypeDeepLink {
            const val kessChatDeepLink = "io.kessinnovation.kesschat"
            const val acledaDeepLink = "com.domain.acledabankqr"
            const val sathapanaDeepLink = "kh.com.sathapana.consumer"
            const val abaDeepLink = "com.paygo24.ibank"
        }

        object TypeScheme {
            const val abaScheme = "abamobilebank"
            const val acledaScheme = "market"
            const val kessChatScheme = "kesspay.io"
            const val spnScheme = "spnb"
        }
    }
}