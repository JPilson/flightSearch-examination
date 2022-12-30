package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.flightsearch.utils.Helpers

@Entity(
    tableName = "tbl_plane",
    indices = [Index(name = "tbl_plane_unique", value = ["name", "IATA", "ICAO"], unique = true)]
)
data class PlaneModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val IATA: String,
    val ICAO: String,
) {
    companion object {
        fun fromString(it: String): PlaneModel {
            val array = it.split(",")
            return PlaneModel(
                0,
                Helpers.formatString(array[0]),
                Helpers.formatString(array[1]),
                Helpers.formatString(array[2])
            )
        }
    }
}
