package com.example.myapplicationview.core.network.repository

import com.example.myapplicationview.core.network.bean.BaseResponse
import com.example.myapplicationview.core.network.bean.NetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository {

    suspend fun <T : Any> callRequest(
        call: suspend () -> NetResult<T>
    ): NetResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            // TODO: unify error handling hooks as needed
            e.printStackTrace()
            when (e) {
                is UnknownHostException,
                is ConnectException -> {
                    // No network
                }
                is SocketTimeoutException -> {
                    // Request timeout
                }
                else -> {
                    // Other error
                }
            }
            NetResult.Error(e)
        }
    }

    suspend fun <T : Any> handleResponse(
        response: BaseResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): NetResult<T> {
        return coroutineScope {
            when (response.code) {
                "200" -> {
                    successBlock?.let { it() }
                    NetResult.Success(response.data)
                }
                "401" -> {
                    // Token expired
                    NetResult.Error(Exception(response.message))
                }
                else -> {
                    errorBlock?.let { it() }
                    NetResult.Error(Exception(response.message))
                }
            }
        }
    }
}
