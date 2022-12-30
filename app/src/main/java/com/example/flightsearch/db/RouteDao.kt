package com.example.flightsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.flightsearch.models.RouteModel
import com.example.flightsearch.models.TicketModel

@Dao
interface RouteDao {
    @Query("Select * from tbl_route LIMIT :page")
    fun getAll(page: Int = 20): List<RouteModel>

    @Transaction
    @Query("Select * from tbl_route INNER JOIN tbl_airline ta on ta.airlineId = tbl_route.airlineId where sourceAirportId = :sourceId and destinationAirportId = :destinationId")
    fun getBySourceAndDestination(sourceId: Int, destinationId: Int): List<TicketModel>

    @Transaction
    @Query("SELECT  *from tbl_route INNER JOIN tbl_airline ta on ta.airlineId = tbl_route.airlineId where tbl_route.sourceAirportId = :sourceId and tbl_route.destinationAirportId = :destinationId")
    fun getBySourceAndDestinationWithAirline(sourceId: Int, destinationId: Int): List<TicketModel>

    @Transaction
    @Query("Select * from tbl_route INNER JOIN tbl_airline ta on ta.airlineId = tbl_route.airlineId where sourceAirportId = :sourceId and destinationAirportId = :destinationId and stops = 0")
    fun getNonStopBySourceAndDestination(sourceId: Int, destinationId: Int): List<TicketModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg data: RouteModel)
}