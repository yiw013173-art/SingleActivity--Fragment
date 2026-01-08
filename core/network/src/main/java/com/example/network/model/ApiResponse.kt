package com.example.network.model

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data:T?
)