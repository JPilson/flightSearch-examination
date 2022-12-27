package com.example.flightsearch.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.models.AirportModel

interface MainDao<T> {
    @Query("Select * from tbl_airport LIMIT :page")
    abstract fun <T>getAll(page:Int = 20):List<T>

    @Query("Select * from tbl_airport where name like :name")
   abstract fun <T>selectByName(name:String):List<T>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun <T>save(vararg data: T)
}