package com.example.flightsearch.repository

import com.example.flightsearch.db.PlaneDao
import com.example.flightsearch.models.PlaneModel

class PlaneRepository(private val dao:PlaneDao) {
    fun register(vararg it: PlaneModel) = dao.save(*it)
    fun getAll(page: Int = 1) = dao.getAll(page)
    fun search(query: String) = dao.selectByName(query)
}