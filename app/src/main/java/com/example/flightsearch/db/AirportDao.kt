package com.example.flightsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.flightsearch.models.AirportModel

@Dao
interface AirportDao {

    @Query("Select * from tbl_airport LIMIT :page")
    fun getAll(page: Int = 20): List<AirportModel>

    @Query("Select * from tbl_airport where lower(name) like :name or lower(city) like :name or lower(country) like :name")
    fun selectByName(name: String): List<AirportModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg data: AirportModel)

    @Query("SELECT  DISTINCT country FROM tbl_airport where lower(country) like :query ORDER BY country")
    fun searchCountry(query: String): List<String>

    @Query("SELECT * FROM TBL_AIRPORT WHERE airportId = :id")
    fun getAirportById(id: Int): AirportModel?

    @Transaction()
    @Query("SELECT DISTINCT t.id,t.name,t.airportId,t.city,t.country,t.IATA,t.ICAO,t.latitude," +
                "t.longitude,t.altitude,t.DST,t.tzDatabase,t.timeZone,t.type,t.source " +
                "from tbl_route" + " inner join tbl_airport t on t.airportId =sourceAirportId" +
                " where destinationAirportId = :destinationId and t.country = :sourceCountry  ;"
    )
    fun searchPossibleAirports(destinationId: Int, sourceCountry: String): List<AirportModel>

}