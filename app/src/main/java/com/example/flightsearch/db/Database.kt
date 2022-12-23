package com.example.flightsearch.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class Database : RoomDatabase() {
    companion object {
        private var INSTANCE: Database? = null
        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, Database::class.java, "flight_db")
                    .build()
                return INSTANCE!!
            }
        }
    }
}