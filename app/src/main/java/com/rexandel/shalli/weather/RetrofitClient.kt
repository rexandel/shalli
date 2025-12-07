package com.rexandel.shalli.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.rexandel.shalli.BuildConfig

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}