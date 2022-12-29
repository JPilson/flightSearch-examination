package com.example.flightsearch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.databinding.FlightTicketBinding
import com.example.flightsearch.models.RouteModel

class RouteListAdapter : RecyclerView.Adapter<RouteListAdapter.RouteViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<RouteModel>() {
        override fun areItemsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            return newItem.areItemsTheSame(oldItem)
        }

        override fun areContentsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            return newItem.areItemsTheSame(oldItem)
        }
    }
    private var onItemClickListener: ((RouteModel) -> Unit)? = null
    private val differ = AsyncListDiffer(this, differCallback)

    inner class RouteViewHolder(val binding: FlightTicketBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(
            FlightTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {

        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setOnItemClickListener(listener: (RouteModel) -> Unit) {
        onItemClickListener = listener
    }

}