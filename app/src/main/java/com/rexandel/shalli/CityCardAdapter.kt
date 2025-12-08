package com.rexandel.shalli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.rexandel.shalli.databinding.CityCardBinding

class CityCardAdapter : RecyclerView.Adapter<CityCardAdapter.CityCardHolder>() {
    val cityCardList = ArrayList<CityCard>()

    class CityCardHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = CityCardBinding.bind(item)

        fun bind(cityCard: CityCard) = with(binding) {
            cityTitle.text = cityCard.cityTitle

            val photoUrl = cityCard.photoUrl

            if (photoUrl != null && photoUrl.isNotEmpty()) {
                cityImage.load(photoUrl) {
                    crossfade(true)
                    placeholder(R.drawable.base_city)
                    error(R.drawable.base_city)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    diskCachePolicy(CachePolicy.ENABLED)
                    size(160, 160)
                }
            } else {
                cityImage.setImageResource(R.drawable.base_city)
            }

            cityCard.weatherData?.let { weather ->
                temperature.text = "${weather.temperature}°C"
                condition.text = weather.condition
                humidity.text = "Humidity: ${weather.humidity}%"
                windSpeed.text = "Wind: ${weather.windSpeed} km/h"
                feelsLike.text = "${weather.feelsLike}°C"
                pressureMb.text = "Pressure: ${weather.pressure_mb} mb"

                temperature.visibility = View.VISIBLE
                condition.visibility = View.VISIBLE
                humidity.visibility = View.VISIBLE
                windSpeed.visibility = View.VISIBLE
                feelsLike.visibility = View.VISIBLE
                pressureMb.visibility = View.VISIBLE
            } ?: run {
                temperature.visibility = View.GONE
                condition.visibility = View.GONE
                humidity.visibility = View.GONE
                windSpeed.visibility = View.GONE
                feelsLike.visibility = View.GONE
                pressureMb.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_card, parent, false)
        return CityCardHolder(view)
    }

    override fun onBindViewHolder(holder: CityCardHolder, position: Int) {
        holder.bind(cityCardList[position])
    }

    override fun getItemCount(): Int {
        return cityCardList.size
    }

    fun addCityCard(cityCard: CityCard) {
        cityCardList.add(cityCard)
        notifyDataSetChanged()
    }

    fun updateCityCardWeather(cityName: String, weatherData: WeatherData) {
        val index = cityCardList.indexOfFirst { it.cityTitle.equals(cityName, ignoreCase = true) }
        if (index != -1) {
            val currentCard = cityCardList[index]
            cityCardList[index] = currentCard.copy(weatherData = weatherData)
            notifyItemChanged(index)
        }
    }

    fun updateCityCardPhoto(cityName: String, photoUrl: String) {
        val index = cityCardList.indexOfFirst { it.cityTitle.equals(cityName, ignoreCase = true) }
        if (index != -1) {
            val currentCard = cityCardList[index]
            cityCardList[index] = currentCard.copy(photoUrl = photoUrl)
            notifyItemChanged(index)
        }
    }
}