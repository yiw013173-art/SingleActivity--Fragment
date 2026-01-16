package com.example.myapplicationview.core.network.bean

data class BaseResponse<T : Any>(
    val code: String,
    val message: String,
    val data: T
)
