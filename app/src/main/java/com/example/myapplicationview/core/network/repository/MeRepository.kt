package com.example.myapplicationview.core.network.repository

import com.example.myapplicationview.core.network.api.MeApiService
import com.example.myapplicationview.core.network.model.MeData
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeRepository @Inject constructor(
    private val service: MeApiService
) {

    private val cacheMap = ConcurrentHashMap<Int, List<MeData>>()

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
