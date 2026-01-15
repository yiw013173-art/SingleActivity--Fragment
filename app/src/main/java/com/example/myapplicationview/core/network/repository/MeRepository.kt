package com.example.myapplicationview.core.network.repository

import com.example.myapplicationview.core.network.api.MeApiService
import com.example.myapplicationview.core.network.model.MeData
import com.example.myapplicationview.core.network.retrofit.RetrofitManager
import java.util.concurrent.ConcurrentHashMap

object MeRepository {

    private val cacheMap = ConcurrentHashMap<Int, List<MeData>>()

    private val service by lazy { RetrofitManager.meCreate(MeApiService::class.java) }

    suspend fun getMeDataCache(page: Int, size: Int): List<MeData> {
        val cacheList = cacheMap.remove(page)
        if (cacheList != null) {
            return cacheList
        }
        return try {
            service.getMeData(page, size)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

 
}
