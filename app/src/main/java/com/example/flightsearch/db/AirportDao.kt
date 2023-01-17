package com.example.flightsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    fun searchCountry(query:String):List<String>

    @Query("SELECT * FROM TBL_AIRPORT WHERE airportId = :id")
    fun getAirportById(id:Int):AirportModel?

}