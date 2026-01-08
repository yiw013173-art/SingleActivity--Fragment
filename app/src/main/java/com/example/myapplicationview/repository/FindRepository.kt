package com.example.myapplicationview.repository

import com.example.network.api.UserApiService
import com.example.network.retrofit.RetrofitManager

object FindRepository {
    private val apiService by lazy {
        RetrofitManager.create(UserApiService::class.java)
    }

    suspend fun getUserInfo(count: Int) = apiService.getUserInfo(count)
}