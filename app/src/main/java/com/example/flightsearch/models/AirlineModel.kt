package com.example.flightsearch.models

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.flightsearch.utils.Helpers

@Entity(
    tableName = "tbl_airline",
    indices = [Index(value =["airlineId"], unique = true),Index(
        name = "name_alias_index",
        value = ["name", "airlineId", "alias"],
        unique = true
    )],
)
data class AirlineModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val airlineId: Int,
    @ColumnInfo(name = "name")  val name: String,
    val alias: String,
    val IATA: String,
    val ICAO: String,
    val callSign: String,
    val country: String,
    val active: String //TODO add as enum
) {
    companion object {
        const val RESTRICT_INDEX = "name_alias_index"
        private const val TAG = "AirlineModel"
        fun fromString(it: String): AirlineModel {
            val split = it.split(",")
            return AirlineModel(
                0,
                split[0].toInt(),
                Helpers.formatString(split[1]),
                Helpers.formatString(split[2]),
                Helpers.formatString(split[3]),
                Helpers.formatString(split[4]),
                Helpers.formatString(split[5]),
                Helpers.formatString(split[6]),
                Helpers.formatString(split[7])

                )
        }


    }
}