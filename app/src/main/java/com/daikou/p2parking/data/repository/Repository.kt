package com.daikou.p2parking.data.repository

import android.content.Context
import com.daikou.p2parking.base.ApiResWraper
import com.daikou.p2parking.model.User
import com.daikou.p2parking.data.remote.ParkingApi
import com.daikou.p2parking.model.LotTypeModel
import com.daikou.p2parking.view_model.LotTypeViewModel

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

    fun fetchLotType(): Flow<ApiResWraper<List<LotTypeModel>>> = flow {
        val respond = apiService.fetchLotType()
        emit(respond)
    }

    fun submitChecking(bodyMap: HashMap<String, Any>): Flow<ApiResWraper<JsonElement>> = flow {
        val respond = apiService.submitChecking(bodyMap)
        emit(respond)
    }
}
