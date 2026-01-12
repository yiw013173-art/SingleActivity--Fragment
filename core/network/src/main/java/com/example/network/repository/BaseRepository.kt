package com.example.network.repository

import com.example.network.model.BaseResponse
import com.example.network.model.NetResult
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
            //这里统一处理异常
            e.printStackTrace()
            when(e) {
                is UnknownHostException,
                is ConnectException -> {
                    // 无网络

                }
                is SocketTimeoutException -> {
                    //请求超时，请稍后重试

                }
                else -> {

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
            when(response.code) {
                "200" -> {
                    successBlock?.let { it() }
                    NetResult.Success(response.data)
                }
                "401" -> {
                    //token失效
                    NetResult.Error(kotlin.Exception(response.message))
                }
                else -> {
                    errorBlock?.let { it() }
                    NetResult.Error(kotlin.Exception(response.message))
                }
            }
        }
    }

}
