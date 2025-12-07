package com.rexandel.shalli.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rexandel.shalli.WeatherData
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _weatherData = MutableLiveData<Pair<String, WeatherData>>()
    val weatherData: LiveData<Pair<String, WeatherData>> get() = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchWeatherByCity(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            val result = repository.getWeatherData(city)

            _isLoading.value = false

            result.onSuccess { weatherResponse ->
                val weatherData = WeatherData(
                    temperature = weatherResponse.current.tempC,
                    condition = weatherResponse.current.condition.text,
                    humidity = weatherResponse.current.humidity,
                    windSpeed = weatherResponse.current.windKph,
                    feelsLike = weatherResponse.current.feelslikeC,
                    pressure_mb = weatherResponse.current.pressureMb,
                )
                _weatherData.value = Pair(city, weatherData)
            }.onFailure { exception ->
                _errorMessage.value = "Couldn't get the data: ${exception.message}"
            }
        }
    }
}