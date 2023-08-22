package com.daikou.p2parking.data.remote

import com.daikou.p2parking.base.ApiResWraper
import com.daikou.p2parking.model.User
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*


interface ParkingApi {

    //login
    @POST("api/auth/login")
    suspend fun login(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    //login
    @POST("api/auth/logout")
    suspend fun logout(): ApiResWraper<JsonElement>

    //user info
    @GET("api/auth/me")
    suspend fun userInfo(): ApiResWraper<User>

    @POST("api/auth/change-password")
    suspend fun submitChangePwd(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @POST("api/auth/requestOTPForgotPassword")
    suspend fun submitRequestOtp(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @POST("api/auth/verifyOTPForgotPassword")
    suspend fun submitVerifyOtp(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @POST("api/auth/changePasswordByOTP")
    suspend fun submitChangePasswordByOtp(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>


    @POST("api/auth/me")
    suspend fun updateUserInfo(@Body map: HashMap<String, Any>): ApiResWraper<User>

    @POST("/api/booking/preview/doCheckout")
    suspend fun submitReviewBooking(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>


    @GET("api/car/search")
    suspend fun submitGetTaxiDriver(@QueryMap map: HashMap<String, Any>): ApiResWraper<JsonElement>


    @POST("api/user/customer/booking/cancelled/{code}")
    suspend fun submitCancelBookingTaxi(
        @Path(
            value = "code",
            encoded = true
        ) code: String,
    ): ApiResWraper<JsonElement>

    //polyline
    @GET("api/1/route")
    fun polylineApi(
        @Query(value = "point") latLngA:String,
        @Query(value = "point") latLngB:String,
        @Query(value = "vehicle") vehicle: String,
        @Query(value = "points_encoded") points_encoded: Boolean,
        @Query(value = "key") key: String
    ): Call<JsonElement>

    // Driver Accept
    @POST("api/driver/accept_booking")
    suspend fun acceptBookingDriverTaxi(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @POST("api/user/customer/booking/cancelled")
    suspend fun cancelBookingCustomer(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>

    @POST("api/user/customer/booking/paid")
    suspend fun submitPaidBookingCustomer(@Body map: HashMap<String, Any>): ApiResWraper<JsonElement>
}
