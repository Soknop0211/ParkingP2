package com.daikou.p2parking.data.remote

import com.daikou.p2parking.base.ApiResWraper
import com.daikou.p2parking.data.model.TicketModel
import com.daikou.p2parking.model.LotTypeModel
import com.google.gson.JsonElement
import retrofit2.http.*

interface ParkingApi {

    //login
    @POST("api/auth/login")
    suspend fun login(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @POST("api/auth/logout")
    suspend fun logout(): ApiResWraper<JsonElement>

    //lot type
    @GET("api/p2/fetch_lot_types")
    suspend fun fetchLotType(): ApiResWraper<List<LotTypeModel>>

    @POST("api/p2/check_in")
    suspend fun submitChecking(@Body map: HashMap<String, Any>): ApiResWraper<TicketModel>

    @POST("api/p2/check_out")
    suspend fun submitCheckOut(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @GET("api/user/fetch_all_parking_devices")
    suspend fun fetchParkingDevices() : ApiResWraper<JsonElement>
}
