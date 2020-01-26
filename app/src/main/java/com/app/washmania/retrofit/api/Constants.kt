package com.app.washmania.retrofit.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


object Constants {


    fun setTimeOutForDatapost(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(loggingInterceptor)
                .build()
    }






    fun setTimeOut(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            //?.addHeader("Content-Type", "application/json")
                            ?.addHeader("Content-Type", "application/json")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }

    fun setTimeOutAfterLogin(rember_token: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            ?.addHeader("Authorization", "bearer $rember_token")
                            ?.addHeader("Content-Type", "application/json")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }

    fun setTimeOutAfterLoginHeaderFormData(rember_token: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            ?.addHeader("Authorization", "bearer $rember_token")
                            ?.addHeader("Content-Type", "Form-data")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }
}