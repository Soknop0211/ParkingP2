package com.daikou.p2parking.base

class ResponseErrorBody<T>(
    val code: Int,
    val error: T,
    val message: String,
    val success: Boolean
)