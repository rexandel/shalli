package com.rexandel.shalli

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rexandel.shalli.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = CityCardAdapter()
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        init()
        observeWeatherData()
        observeCityPhotos()
        observeErrors()
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
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeWeatherData() {
        appViewModel.weatherData.observe(this) { (cityName, weatherData) ->
            adapter.updateCityCardWeather(cityName, weatherData)
        }
    }

    private fun observeCityPhotos() {
        appViewModel.cityPhotoUrl.observe(this) { (cityName, photoUrl) ->
            adapter.updateCityCardPhoto(cityName, photoUrl)
        }
    }

    private fun observeErrors() {
        appViewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addCityCard(cityName: String) {
        val cityCard = CityCard(cityName)
        adapter.addCityCard(cityCard)
        appViewModel.fetchWeatherByCity(cityName)
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