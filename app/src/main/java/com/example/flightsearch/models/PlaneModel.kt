package com.example.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tbl_plane")
data class PlaneModel(
    @PrimaryKey val id: Int,
    val name: String,
    val IATA: String,
    val ICAO: String,
){

}
