package com.example.network.api

import com.example.network.model.BaseResponse
import com.example.network.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("api/")
    suspend fun getUserInfo(@Query("results") count: Int): BaseResponse<UserResponse>
}