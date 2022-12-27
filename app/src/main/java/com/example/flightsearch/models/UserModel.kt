package com.example.flightsearch.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class UserModel(
    @PrimaryKey val id:Int,
    @ColumnInfo var name:String)
