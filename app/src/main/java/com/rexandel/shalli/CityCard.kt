package com.rexandel.shalli

data class CityCard(
    val cityImageId: Int,
    val cityTitle: String,
    var weatherData: WeatherData? = null
)

data class WeatherData(
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val feelsLike: Double,
    val pressure_mb: Double
)