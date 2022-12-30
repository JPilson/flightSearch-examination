package com.example.flightsearch.models

import android.util.Log
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.flightsearch.utils.Helpers

@Entity(
    tableName = "tbl_route",
    foreignKeys = [
        ForeignKey(
            entity = AirlineModel::class,
            parentColumns = ["airlineId"],
            childColumns = ["airlineId"],
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = AirportModel::class,
            parentColumns = ["airportId"],
            childColumns = ["sourceAirportId"],
            onUpdate = CASCADE
        ), ForeignKey(
            entity = AirportModel::class,
            parentColumns = ["airportId"],
            childColumns = [ "destinationAirportId"],
            onUpdate = CASCADE
        ),
    ]
)
data class RouteModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
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
        private const val TAG = "RouteModel"
        fun fromString(it: String): RouteModel {
            Log.d(TAG, "fromString: $it")
            val split = it.split(",")
            return RouteModel(
                0,
                Helpers.formatString(split[0]),
                Helpers.formatString(split[1]),
                Helpers.formatString(split[2]),
                Helpers.formatString(split[3]),
                Helpers.formatString(split[4]),
                Helpers.formatString(split[5]),
                Helpers.formatString(split[6]),
                Helpers.formatString(split[7]).toInt(),
                Helpers.formatString(split[8]),
            )
        }

    }
}

