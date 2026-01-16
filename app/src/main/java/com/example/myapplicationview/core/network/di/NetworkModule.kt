package com.example.myapplicationview.core.network.di

import com.example.myapplicationview.core.network.api.MeApiService
import com.example.myapplicationview.core.network.api.UserApiService
import com.example.myapplicationview.core.network.interceptor.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RandomUserRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JsonPlaceholderRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val RANDOM_USER_BASE_URL = "https://randomuser.me/"
    private const val JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    fun provideLoggingInterceptor(): LoggingInterceptor = LoggingInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: LoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @RandomUserRetrofit
    fun provideRandomUserRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RANDOM_USER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @JsonPlaceholderRetrofit
    fun provideJsonPlaceholderRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(JSON_PLACEHOLDER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideUserApiService(@RandomUserRetrofit retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    fun provideMeApiService(@JsonPlaceholderRetrofit retrofit: Retrofit): MeApiService {
        return retrofit.create(MeApiService::class.java)
    }
}
