package com.example.network.model

sealed class NetResult<out T> {
    data class Success<out T>(val data: T) : NetResult<T>()
    data class Error(val exception: Throwable) : NetResult<Nothing>()
}

data class BaseResponse<T>(
    val code: String = "200",
    val message: String = "",
    val data: T,
)