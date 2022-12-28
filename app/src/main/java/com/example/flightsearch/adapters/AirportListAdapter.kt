package com.example.flightsearch.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.databinding.AirportPickerBinding
import com.example.flightsearch.models.AirportModel


class AirportListAdapter(private val onlyAirport: Boolean = false) :
    RecyclerView.Adapter<AirportListAdapter.AirportViewHolder>() {
    inner class AirportViewHolder(val binding: AirportPickerBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((AirportModel) -> Unit)? = null
    fun setOnItemClickListener(listener: (AirportModel) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<AirportModel>() {
        override fun areContentsTheSame(oldItem: AirportModel, newItem: AirportModel): Boolean {
            return ((oldItem.name == newItem.name).and(oldItem.airportId == newItem.airportId))
        }

        override fun areItemsTheSame(oldItem: AirportModel, newItem: AirportModel): Boolean {
            return oldItem.airportId == newItem.airportId

        }
    }
    private val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder {
        return AirportViewHolder(
            AirportPickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            if (!onlyAirport) {
                label.visibility = View.INVISIBLE
                icon.visibility = View.INVISIBLE
            }
            airportName.text = currentItem.name
            countryName.text = "${currentItem.country} - ${currentItem.city}"
            root.setOnClickListener {
                onItemClickListener?.let { it(currentItem) }
            }
        }
    }

    fun setData(airport: List<AirportModel>) {
        differ.submitList(airport)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}