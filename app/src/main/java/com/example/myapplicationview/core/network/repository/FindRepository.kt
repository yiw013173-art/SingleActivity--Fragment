package com.example.myapplicationview.core.network.repository

import com.example.myapplicationview.core.network.api.UserApiService
import com.example.myapplicationview.core.network.model.UserDto
import com.example.myapplicationview.core.network.model.UserResponse
import com.example.myapplicationview.core.network.retrofit.RetrofitManager
import java.util.concurrent.ConcurrentHashMap

object FindRepository {
    private val apiService by lazy {
        RetrofitManager.create(UserApiService::class.java)
    }

    private val pageCache = ConcurrentHashMap<Int, List<UserDto>>()

    suspend fun getUserInfoWithCache(count: Int, page: Int): UserResponse {
        val cacheList = pageCache.remove(page)
        if (cacheList != null) {
            return UserResponse(results = cacheList)
        }
        return apiService.getUserInfoWithIndex(count, page)
    }

    suspend fun preloadData(count: Int, page: Int) {
        try {
            if (!pageCache.containsKey(page)) {
                val response = apiService.getUserInfoWithIndex(count, page)
                pageCache[page] = response.results
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
