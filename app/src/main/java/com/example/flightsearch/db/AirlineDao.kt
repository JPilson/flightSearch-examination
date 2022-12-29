package com.example.flightsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.models.AirlineModel
@Dao
interface AirlineDao {

    @Query("Select * from tbl_airline LIMIT :page")
    fun getAll(page:Int = 20):List<AirlineModel>

    @Query("Select * from tbl_airline where name like :name")
    fun selectByName(name:String):List<AirlineModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg data:AirlineModel)



}