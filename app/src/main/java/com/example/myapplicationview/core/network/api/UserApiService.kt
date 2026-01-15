package com.example.myapplicationview.core.network.api

import com.example.myapplicationview.core.network.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("api/")
    suspend fun getUserInfoWithIndex(@Query("results") count: Int, @Query("page") page: Int): UserResponse
}
