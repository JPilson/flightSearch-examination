package com.example.flightsearch.repository

import com.example.flightsearch.db.AirportDao
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel

class AirportRepository(private val dao: AirportDao) {
    fun registerAirport(vararg it: AirportModel) = dao.save(*it)
    fun getAll(page: Int = 1) = dao.getAll(page)
    fun search(query: String) = dao.selectByName(query)
    fun searchCountry(query: String) = dao.searchCountry(query)
    fun getAirportById(id:Int) = dao.getAirportById(id)
}