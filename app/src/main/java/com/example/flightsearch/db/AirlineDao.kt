package com.example.flightsearch.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.models.AirlineModel
interface AirlineDao {

    @Query("Select * from tbl_airport LIMIT :page")
    fun getAll(page:Int = 20):List<AirlineModel>

    @Query("Select * from tbl_airport where name like :name")
    fun selectByName(name:String):List<AirlineModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg data:AirlineModel)



}