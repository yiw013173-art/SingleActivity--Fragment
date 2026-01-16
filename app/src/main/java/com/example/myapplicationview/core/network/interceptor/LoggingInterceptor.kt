package com.example.myapplicationview.core.network.interceptor

import com.example.myapplicationview.core.util.logI
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()

        "LoggingInterceptor Sending request: ${request.method} ${request.url}".logI()
        "LoggingInterceptor Headers: ${request.headers}".logI()
        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = body.contentType()?.charset(Charset.forName("UTF-8")) ?: Charset.forName("UTF-8")
            "LoggingInterceptor Request Body: ${buffer.readString(charset)}".logI()
        }

        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        val responseBody = response.peekBody(1024 * 1024)
        "LoggingInterceptor Received response for ${response.request.url} in ${TimeUnit.NANOSECONDS.toMillis(t2 - t1)}ms".logI()
        "LoggingInterceptor Status Code: ${response.code}".logI()
        "LoggingInterceptor Response Body: ${responseBody.string().prettyJson()}".logI()

        return response
    }
}

private fun String.prettyJson(): String {
    return try {
        val jsonElement = JsonParser().parse(this)
        GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
    } catch (e: Exception) {
        this
    }
}
