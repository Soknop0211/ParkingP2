package com.daikou.p2parking.data.repository

import android.content.Context
import com.daikou.p2parking.base.ApiResWraper
import com.daikou.p2parking.model.User
import com.daikou.p2parking.data.remote.ParkingApi

import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository
@Inject constructor(
    private val context: Context,
    private val apiService: ParkingApi,
) {

    fun login(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> = flow {
        val respond = apiService.login(bodyMap)
        emit(respond)
    }

    fun logout(): Flow<ApiResWraper<JsonElement>> = flow {
        val respond = apiService.logout()
        emit(respond)
    }

    fun userInfo(): Flow<ApiResWraper<User>> = flow {
        val respond = apiService.userInfo()
        emit(respond)
    }

    fun submitChangePwd(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> = flow {
        val respond = apiService.submitChangePwd(bodyMap)
        emit(respond)
    }

    fun submitRequestOtp(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> = flow {
        val respond = apiService.submitRequestOtp(bodyMap)
        emit(respond)
    }

    fun submitVerifyOtp(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> = flow {
        val respond = apiService.submitVerifyOtp(bodyMap)
        emit(respond)
    }

    fun submitChangePasswordByOtp(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> =
        flow {
            val respond = apiService.submitChangePasswordByOtp(bodyMap)
            emit(respond)
        }

    fun updateUserInfo(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<User>> = flow {
        val respond = apiService.updateUserInfo(bodyMap)
        emit(respond)
    }


    fun submitGetTaxiDriver(
        map: HashMap<String, Any>,
    ): Flow<ApiResWraper<JsonElement>> =
        flow {
            val respond = apiService.submitGetTaxiDriver(map)
            emit(respond)
        }

    fun submitCancelBookingTaxiV2(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> =
        flow {
            val respond = apiService.cancelBookingCustomer(bodyMap)
            emit(respond)
        }

    fun submitAcceptBookingTaxi(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> =
        flow {
            val respond = apiService.acceptBookingDriverTaxi(bodyMap)
            emit(respond)
        }

    fun submitPaidBookingCustomer(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> =
        flow {
            val respond = apiService.submitPaidBookingCustomer(bodyMap)
            emit(respond)
        }
}
