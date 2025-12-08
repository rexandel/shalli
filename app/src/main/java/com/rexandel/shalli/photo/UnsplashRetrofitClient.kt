package com.rexandel.shalli.photo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.rexandel.shalli.BuildConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object UnsplashRetrofitClient {
    private const val BASE_URL = BuildConfig.CITY_IMG_BASE_URL


    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)

        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept-Version", "v1")
                .build()
            chain.proceed(request)
        }
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val unsplashApiService: UnsplashApiService by lazy {
        retrofit.create(UnsplashApiService::class.java)
    }
}