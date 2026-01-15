package com.example.network.repository

import com.example.network.api.MeApiService
import com.example.network.model.MeData
import com.example.network.retrofit.RetrofitManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object MeRepository {

    private val cacheMap = ConcurrentHashMap<Int, List<MeData>>()

    private val service by lazy { RetrofitManager.meCreate(MeApiService::class.java) }

   suspend fun getMeDataCache(page: Int,size: Int): List<MeData> {
        if (cacheMap.containsKey(page)){
            val cacheList = cacheMap[page]!!
            cacheMap[page] = emptyList()
            return cacheList
        }
        try {
            val cacheList = service.getMeData(page,size)
            return cacheList
        }catch (e:Exception){
            e.printStackTrace()
            return emptyList()
        }
    }

   suspend fun preloadCache(page: Int,size: Int) {
        if (!cacheMap.containsKey(page)){
           cacheMap[page] = service.getMeData(page,size)
        }
    }
}