package com.rexandel.shalli

data class CityCard(
    val cityTitle: String,
    var weatherData: WeatherData? = null,
    var photoUrl: String? = null
)

data class WeatherData(
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val feelsLike: Double,
    val pressure_mb: Double
)