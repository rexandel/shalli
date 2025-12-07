package com.rexandel.shalli.weather

import android.util.Log
import java.io.IOException
import com.rexandel.shalli.BuildConfig

class WeatherRepository {
    private val apiService = RetrofitClient.weatherApiService
    private val apiKey = BuildConfig.API_KEY

    suspend fun getWeatherData(city: String): Result<WeatherResponse> {
        Log.d("WeatherRepo", "Starting weather request for city: $city")

        return try {
            val response = apiService.getCurrentWeather(city, apiKey)
            Log.d("WeatherRepo", "Response received. Code: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    Log.i("WeatherRepo", "Weather for $city: ${weatherResponse.current.tempC}°C, ${weatherResponse.current.condition.text}")
                    Result.success(weatherResponse)
                } ?: run {
                    Log.w("WeatherRepo", "Empty response body for city: $city")
                    Result.failure(Exception("Empty response from server"))
                }
            } else {
                val errorMessage = response.errorBody()?.use { errorBody ->
                    try {
                        val errorText = errorBody.string()
                        when (response.code()) {
                            400 -> "Invalid city name"
                            401 -> "Invalid API key"
                            404 -> "City '$city' not found"
                            429 -> "Too many requests. Please wait"
                            else -> "Error ${response.code()}: $errorText"
                        }
                    } catch (e: IOException) {
                        "Error ${response.code()}: ${response.message()}"
                    }
                } ?: "Error ${response.code()}: ${response.message()}"

                Log.e("WeatherRepo", "Error for $city: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Log.e("WeatherRepo", "Network error for $city", e)
            Result.failure(Exception("No internet connection: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Log.e("WeatherRepo", "Unexpected error for $city", e)
            Result.failure(Exception("Error: ${e.localizedMessage}"))
        } finally {
            Log.d("WeatherRepo", "Request completed for $city")
        }
    }
}