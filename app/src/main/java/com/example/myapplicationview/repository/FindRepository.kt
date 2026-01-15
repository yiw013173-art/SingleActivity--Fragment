package com.example.myapplicationview.repository

import com.example.network.api.UserApiService
import com.example.network.model.UserDto
import com.example.network.model.UserResponse
import com.example.network.retrofit.RetrofitManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object FindRepository {
    private val apiService by lazy {
        RetrofitManager.create(UserApiService::class.java)
    }

    private val pageCache = ConcurrentHashMap<Int, List<UserDto>>()

    suspend fun getUserInfo(count: Int) = apiService.getUserInfo(count)

    suspend fun getUserInfoWithIndex(count: Int, page: Int) = apiService.getUserInfoWithIndex(count, page)

    suspend fun getUserInfoWithCache(count: Int,page: Int): UserResponse{ //先从缓存里取，没有就请求网络
        if (pageCache.containsKey(page)){
            val cacheList = pageCache[page]!!
            pageCache.remove(page) //取出来就删除，节省内存
            return UserResponse(results = cacheList)
        }
        return apiService.getUserInfoWithIndex(count,page)
    }

    suspend fun preloadData(count: Int, page: Int) { //预加载数据到缓存
        try {
            if (!pageCache.containsKey(page)){
                val response = apiService.getUserInfoWithIndex(count, page)
                pageCache[page] = response.results
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}