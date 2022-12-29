package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flightsearch.utils.Helpers

@Entity(tableName = "tbl_plane")
data class PlaneModel(
    @PrimaryKey val id: Int,
    val name: String,
    val IATA: String,
    val ICAO: String,
) {
    companion object {
        fun fromString(it: String): PlaneModel {
            val array = it.split(",")
            return PlaneModel(
                array[0].toInt(),
                Helpers.formatString(array[1]),
                Helpers.formatString(array[2]),
                Helpers.formatString(array[3])
            )
        }
    }
}
