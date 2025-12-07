package com.rexandel.shalli

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rexandel.shalli.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = CityCardAdapter()
    private val imageIdList = listOf(
        R.drawable.krasnodar,
        R.drawable.moscow,
        R.drawable.saint_petersburg
    )

    private var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            cityWeather.layoutManager = LinearLayoutManager(this@MainActivity)
            cityWeather.adapter = adapter
            addCityCard.setOnClickListener {
                if (index > 2) index = 0

                val cityCard = CityCard(imageIdList[index], "City ${index+1}")
                adapter.addCityCard(cityCard)
                index++
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
}