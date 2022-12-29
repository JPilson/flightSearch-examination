package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_route")
data class RouteModel(
    @PrimaryKey val id: Int,
    val airline: String,
    val airlineId: String,
    val sourceAirport: String,
    val sourceAirportId: String,
    val destinationAirport: String,
    val destinationAirportId: String,
    val codeShare: String,
    val stops: Int,
    val equipment: String,

    ) {

    fun areItemsTheSame(other: RouteModel): Boolean = (this.copy(id = other.id) == other)
    companion object {
        fun fromString(it: String): RouteModel {
            val split = it.split(",")
            return RouteModel(
                0,
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],
                split[0].toInt(),
                split[0],
            )
        }

    }
}

