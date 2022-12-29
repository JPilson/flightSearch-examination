package com.example.flightsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.models.PlaneModel

@Dao
interface PlaneDao {
    @Query("Select * from tbl_plane LIMIT :page")
    fun getAll(page:Int = 20):List<PlaneModel>

    @Query("Select * from tbl_plane where name like :name")
    fun selectByName(name:String):List<PlaneModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg data: PlaneModel)
}