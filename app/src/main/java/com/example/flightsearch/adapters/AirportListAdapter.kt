package com.example.flightsearch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.databinding.CustomRowBinding
import com.example.flightsearch.models.AirportModel

class AirportListAdapter : RecyclerView.Adapter<AirportListAdapter.AirportViewHolder>() {
    inner class AirportViewHolder(val binding:CustomRowBinding) : RecyclerView.ViewHolder(binding.root)
    private val differCallback = object: DiffUtil.ItemCallback<AirportModel>(){
        override fun areContentsTheSame(oldItem: AirportModel, newItem: AirportModel): Boolean {
            return ((oldItem.name == newItem.name).and(oldItem.airportId == newItem.airportId))
        }

        override fun areItemsTheSame(oldItem: AirportModel, newItem: AirportModel): Boolean {
            return oldItem.airportId == newItem.airportId
        }
    }
    private val differ = AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder {
        return AirportViewHolder(CustomRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            id.text = currentItem.airportId.toString()
            name.text = currentItem.name

        }
    }
    fun setData(airport:List<AirportModel>) {
       differ.submitList(airport)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}