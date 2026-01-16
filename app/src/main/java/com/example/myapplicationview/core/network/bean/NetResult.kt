package com.example.myapplicationview.core.network.bean

sealed class NetResult<out T : Any> {
    data class Success<T : Any>(val data: T) : NetResult<T>()
    data class Error(val exception: Throwable) : NetResult<Nothing>()
}
