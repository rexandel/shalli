package com.rexandel.shalli

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rexandel.shalli.databinding.ActivityMainBinding
import com.rexandel.shalli.weather.WeatherViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = CityCardAdapter()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val imageIdList = listOf(
        R.drawable.krasnodar,
        R.drawable.moscow,
        R.drawable.saint_petersburg
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        init()
        observeWeatherData()
    }

    private fun init() {
        binding.apply {
            cityWeather.layoutManager = LinearLayoutManager(this@MainActivity)
            cityWeather.adapter = adapter

            addCityCard.setOnClickListener {
                showAddCityDialog()
            }
            cityWeather.setHasFixedSize(true)
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }

    private fun observeWeatherData() {
        weatherViewModel.weatherData.observe(this) { (cityName, weatherData) ->
            adapter.updateCityCardWeather(cityName, weatherData)
        }

        weatherViewModel.isLoading.observe(this) { isLoading ->
            // Show/hide the loading indicator
        }

        weatherViewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addCityCard(cityName: String) {
        val randomImage = imageIdList.random()
        val cityCard = CityCard(randomImage, cityName)
        adapter.addCityCard(cityCard)

        weatherViewModel.fetchWeatherByCity(cityName)
    }

    fun showAddCityDialog() {
        binding.dialogContainer.visibility = View.VISIBLE
        binding.dialogContainer.alpha = 0f

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.dialogContainer, AddCityCardFragment.newInstance())
            .commit()

        binding.dialogContainer.animate()
            .alpha(1f)
            .setDuration(200)
            .setListener(null)
            .start()
    }

    fun hideDialogContainer() {
        binding.dialogContainer.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                binding.dialogContainer.visibility = View.GONE
                binding.dialogContainer.alpha = 1f
            }
            .start()
    }

    fun hideKeyboard() {
        val imm = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    fun showKeyboard(editText: EditText) {
        val imm = getSystemService(InputMethodManager::class.java)
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}