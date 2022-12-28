package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_airport")
data class AirportModel(
    @PrimaryKey val id: Int,
    val airportId: Int,
    val name: String,
    val city: String,
    val country: String,
    val IATA: String?,
    val ICAO: String?,
    val latitude: String,
    val longitude: String,
    val altitude: String,
    val DST: String,
    val tzDatabase: String?,
    val timeZone: String,
    val type: String,
    val source: String
) {
    companion object {
        fun fromString(it: String): AirportModel {
            val split = it.split(",")
            return AirportModel(
                0,
                split[0].toInt(),
                split[1],
                split[2],
                split[3],
                split[4],
                split[5],
                split[6],
                split[7],
                split[8],
                split[9],
                split[10],
                split[11],
                split[12],
                split[13]
            )
        }
    }
}
