package com.example.flightsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.models.RouteModel

@Dao
interface RouteDao {
    @Query("Select * from tbl_route LIMIT :page")
    fun getAll(page: Int = 20): List<RouteModel>

    @Query("Select * from tbl_route where sourceAirportId = :sourceId and destinationAirportId = :destinationId")
    fun getBySourceAndDestination(sourceId: Int, destinationId: Int): List<RouteModel>

    @Query("Select * from tbl_route where sourceAirportId = :sourceId and destinationAirportId = :destinationId and stops = 0")
    fun getNonStopBySourceAndDestination(sourceId: Int, destinationId: Int): List<RouteModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg data: RouteModel)
}