package com.example.flightsearch.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.log

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
        private const val TAG = "AirportModel"
        fun fromString(it: String): AirportModel {
            Log.d(TAG, "fromString: $it")
            val split = it.split(",")

            val x = AirportModel(
                split[0].toInt(),
                split[0].toInt(),
                formatString(split[1]),
                formatString(split[2]),
                formatString(split[3]),
                formatString(split[4]),
                formatString(split[5]),
                formatString(split[6]),
                formatString(split[7]),
                formatString(split[8]),
                formatString(split[9]),
                formatString(split[10]),
                formatString(split[11]),
                formatString(split[12]),
                formatString(split[13])
            )
            Log.d(TAG, "fromString: ${x.name}")
            return x
        }

        private fun formatString(it: String): String {
            var x = it.replace("\"", "")
            return x
        }
    }
}
