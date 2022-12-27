package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_airline")
data class AirlineModel(
    @PrimaryKey val id: Int,
    val airLineId: String,
    val name: String,
    val alias: String,
    val IATA: String,
    val ICAO: String,
    val callSign: String,
    val country: String,
    val active: String //TODO add as enum
) {
    companion object {
        fun fromString(it: String): AirlineModel {
            val split = it.split(",")
            return AirlineModel(
                0,
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],
                split[0],

                )
        }


    }
}