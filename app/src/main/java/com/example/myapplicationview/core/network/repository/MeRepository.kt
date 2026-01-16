package com.example.myapplicationview.core.network.repository

import com.example.myapplicationview.core.network.api.MeApiService
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.MeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeRepository @Inject constructor(
    private val service: MeApiService
) : BaseRepository() {

    private val _meFlow = MutableStateFlow<NetResult<List<MeData>>?>(null)
    val meFlow: StateFlow<NetResult<List<MeData>>?> = _meFlow.asStateFlow()

    suspend fun fetchMe(page: Int, size: Int): NetResult<List<MeData>> {
        return callRequest { NetResult.Success(service.getMeData(page, size)) }
    }

    suspend fun preloadMe(page: Int, size: Int) {
        clearMe()
        refreshMe(page, size)
    }

    suspend fun refreshMe(page: Int, size: Int) {
        _meFlow.value = fetchMe(page, size)
    }

    fun clearMe() {
        _meFlow.value = null
    }
}
