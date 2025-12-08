package com.rexandel.shalli.photo

import com.rexandel.shalli.BuildConfig

class CityImageRepository {
    private val apiService = UnsplashRetrofitClient.unsplashApiService
    private val clientId = BuildConfig.CITY_IMG_API_KEY

    suspend fun getCityImageUrl(cityName: String): String? {
        return try {
            val response = apiService.searchPhotos(
                query = cityName,
                perPage = 1,
                clientId = clientId
            )

            if (response.results.isNotEmpty()) {
                val imageUrl = response.results[0].urls.thumb
                imageUrl
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}