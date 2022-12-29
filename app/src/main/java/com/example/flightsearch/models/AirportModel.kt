package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flightsearch.utils.Helpers

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
                split[0].toInt(),
                split[0].toInt(),
                Helpers.formatString(split[1]),
                Helpers.formatString(split[2]),
                Helpers.formatString(split[3]),
                Helpers.formatString(split[4]),
                Helpers.formatString(split[5]),
                Helpers.formatString(split[6]),
                Helpers.formatString(split[7]),
                Helpers.formatString(split[8]),
                Helpers.formatString(split[9]),
                Helpers.formatString(split[10]),
                Helpers.formatString(split[11]),
                Helpers.formatString(split[12]),
                Helpers.formatString(split[13])
            )


        }


    }
}
