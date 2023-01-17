package com.example.flightsearch.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.databinding.AirportPickerBinding
import com.example.flightsearch.databinding.CountryViewBinding
import com.example.flightsearch.models.AirportModel


class CountryListAdapter() :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {
    inner class CountryViewHolder(val binding: CountryViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }


    }
    private val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            CountryViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {

            countryName.text = currentItem
            root.setOnClickListener {
                onItemClickListener?.let { it(currentItem) }
            }
        }
    }

    fun setData(countries: List<String>) {
        differ.submitList(countries)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}