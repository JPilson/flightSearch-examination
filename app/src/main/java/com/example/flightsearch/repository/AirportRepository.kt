package com.example.flightsearch.repository

import com.example.flightsearch.db.AirportDao
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel

class AirportRepository(private val dao: AppDatabase) {
    fun registerAirport(vararg it: AirportModel) = dao.airportDao().save(*it)
    fun getAll(page: Int = 1) = dao.airportDao().getAll(page)
     fun search(query:String) = dao.airportDao().selectByName(query)
}