package com.example.myapplicationview.core.network.repository

import com.example.myapplicationview.core.network.api.UserApiService
import com.example.myapplicationview.core.network.bean.NetResult
import com.example.myapplicationview.core.network.model.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindRepository @Inject constructor(
    private val apiService: UserApiService
) : BaseRepository() {

    private val _userFlow = MutableStateFlow<NetResult<UserResponse>?>(null)
    val userFlow: StateFlow<NetResult<UserResponse>?> = _userFlow.asStateFlow()

    suspend fun fetchUsers(count: Int, page: Int): NetResult<UserResponse> {
        return callRequest { NetResult.Success(apiService.getUserInfoWithIndex(count, page)) }
    }

    suspend fun preloadUsers(count: Int, page: Int) {
        clearUsers()
        refreshUsers(count, page)
    }

    suspend fun refreshUsers(count: Int, page: Int) {
        _userFlow.value = fetchUsers(count, page)
    }

    fun clearUsers() {
        _userFlow.value = null
    }
}
