package com.example.flightsearch.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.flightsearch.models.*

@androidx.room.Database(
    entities = [
        UserModel::class, AirportModel::class,
        AirlineModel::class, RouteModel::class, PlaneModel::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun routeDao(): RouteDao
    abstract fun airlineDao(): AirlineDao
    abstract fun planeDao(): PlaneDao

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
        fun resetCursor(context: Context) {
            INSTANCE?.close()
            INSTANCE = null
            getDatabase(context)
        }
    }
}