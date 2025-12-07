package com.rexandel.shalli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rexandel.shalli.databinding.CityCardBinding


class CityCardAdapter: RecyclerView.Adapter<CityCardAdapter.CityCardHolder>() {
    val cityCardList = ArrayList<CityCard>()

    class CityCardHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CityCardBinding.bind(item)

        fun bind(cityCard: CityCard) = with(binding) {
            cityImage.setImageResource(cityCard.cityImageId)
            cityTitle.text = cityCard.cityTitle
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
}