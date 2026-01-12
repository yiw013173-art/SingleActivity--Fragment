package com.example.network.repository

import com.example.network.api.UserApiService
import com.example.network.model.NetResult
import com.example.network.model.UserResponse
import com.example.network.retrofit.RetrofitManager

object FindRepository : BaseRepository() {
    private val apiService by lazy {
        RetrofitManager.create(UserApiService::class.java)
    }

    suspend fun getUserInfo(count: Int): NetResult<UserResponse> {
        return callRequest {
            handleResponse(apiService.getUserInfo( count))
        }
    }
}