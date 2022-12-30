package com.example.flightsearch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.databinding.FlightTicketBinding
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.models.RouteModel
import com.example.flightsearch.models.TicketModel

class RouteListAdapter : RecyclerView.Adapter<RouteListAdapter.RouteViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<TicketModel>() {
        override fun areItemsTheSame(oldItem: TicketModel, newItem: TicketModel): Boolean {
            return newItem.route.areItemsTheSame(oldItem.route)
        }

        override fun areContentsTheSame(oldItem: TicketModel, newItem: TicketModel): Boolean {
            return newItem.route.areItemsTheSame(oldItem.route)
        }
    }
    private var onItemClickListener: ((TicketModel) -> Unit)? = null
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
            sourceAirport.text = currentItem.route.sourceAirport
            destinationAirport.text = currentItem.route.destinationAirport
            airlineName.text = currentItem?.airline?.name
            root.setOnClickListener {
                onItemClickListener?.let { it1 -> it1(currentItem) }
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setData(routes: List<TicketModel>) {
        differ.submitList(routes)
    }

    fun setOnItemClickListener(listener: (TicketModel) -> Unit) {
        onItemClickListener = listener
    }

}