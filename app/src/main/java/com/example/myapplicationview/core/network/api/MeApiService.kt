package com.example.myapplicationview.core.network.api

import com.example.myapplicationview.core.network.model.MeData
import retrofit2.http.GET
import retrofit2.http.Query

interface MeApiService {
    @GET("posts")
    suspend fun getMeData(@Query("_page") page: Int,@Query("_limit") limit: Int): List<MeData>
}