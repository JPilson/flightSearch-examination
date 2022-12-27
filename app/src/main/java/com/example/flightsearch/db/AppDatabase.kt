package com.example.flightsearch.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flightsearch.models.UserModel

@androidx.room.Database(entities = [UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "flight_db")
                    .build()
                return INSTANCE!!
            }
        }
    }
}