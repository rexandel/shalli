package com.rexandel.shalli

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rexandel.shalli.photo.CityImageRepository
import com.rexandel.shalli.weather.WeatherRepository
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()
    private val cityImageRepository = CityImageRepository()

    private val _weatherData = MutableLiveData<Pair<String, WeatherData>>()
    val weatherData: LiveData<Pair<String, WeatherData>> = _weatherData

    private val _cityPhotoUrl = MutableLiveData<Pair<String, String>>()
    val cityPhotoUrl: LiveData<Pair<String, String>> = _cityPhotoUrl

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchWeatherByCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            launch {
                fetchWeatherData(cityName)
            }

            launch {
                fetchCityPhoto(cityName)
            }

            _isLoading.value = false
        }
    }

    private suspend fun fetchWeatherData(cityName: String) {
        try {
            val weatherResult = weatherRepository.getWeatherData(cityName)

            if (weatherResult.isSuccess) {
                val weatherResponse = weatherResult.getOrNull()
                weatherResponse?.let { response ->
                    val extractedData = WeatherData(
                        temperature = response.current?.tempC ?: 0.0,
                        condition = response.current?.condition?.text ?: "",
                        humidity = response.current?.humidity ?: 0,
                        windSpeed = response.current?.windKph ?: 0.0,
                        feelsLike = response.current?.feelslikeC ?: 0.0,
                        pressure_mb = response.current?.pressureMb ?: 0.0
                    )
                    _weatherData.value = Pair(cityName, extractedData)
                }
            } else {
                val error = weatherResult.exceptionOrNull()
                val errorMsg = error?.message ?: "Failed to fetch weather data"
                _errorMessage.value = errorMsg
            }
        } catch (e: Exception) {
        }
    }

    private suspend fun fetchCityPhoto(cityName: String) {
        try {
            val photoUrl = cityImageRepository.getCityImageUrl(cityName)
            if (photoUrl != null) {
                _cityPhotoUrl.value = Pair(cityName, photoUrl)
            }
        } catch (e: Exception) {
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }
}