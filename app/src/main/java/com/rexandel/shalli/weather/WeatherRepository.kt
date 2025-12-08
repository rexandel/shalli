package com.rexandel.shalli.weather

import java.io.IOException
import com.rexandel.shalli.BuildConfig
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WeatherRepository {
    private val apiService = WeatherRetrofitClient.weatherApiService
    private val apiKey = BuildConfig.WEATHER_API_KEY

    suspend fun getWeatherData(city: String): Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeather(city, apiKey)

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    Result.success(weatherResponse)
                } ?: run {
                    Result.failure(Exception("No weather data available for $city"))
                }
            } else {
                handleErrorResponse(response.code(), city)
            }
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timeout. Please check your internet and try again"))
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load weather: ${e.localizedMessage}"))
        }
    }

    private fun handleErrorResponse(code: Int, city: String): Result<WeatherResponse> {
        val errorMessage = when (code) {
            400 -> "Invalid city name: $city"
            401 -> "Invalid API key. Please check configuration"
            403 -> "Access forbidden. Please check API permissions"
            404 -> "City '$city' not found"
            429 -> "Too many requests. Please wait before trying again"
            500 -> "Weather server error. Please try again later"
            503 -> "Weather service unavailable"
            else -> "Error $code: Failed to get weather for $city"
        }

        return Result.failure(Exception(errorMessage))
    }
}