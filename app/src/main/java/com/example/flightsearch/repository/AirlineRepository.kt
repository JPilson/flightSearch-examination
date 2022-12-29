package com.example.flightsearch.repository

import com.example.flightsearch.db.AirlineDao
import com.example.flightsearch.models.AirlineModel

class AirlineRepository(private val dao: AirlineDao) {
    fun register(vararg it: AirlineModel) = dao.save(*it)
    fun getAll(page: Int = 1) = dao.getAll(page)
    fun search(query: String) = dao.selectByName(query)
}