package com.example.flightsearch.repository

import com.example.flightsearch.db.RouteDao
import com.example.flightsearch.models.RouteModel

class RouteRepository(private val dao: RouteDao) {

    fun register(vararg it: RouteModel) = dao.save(*it)
    fun getAll(page: Int = 1) = dao.getAll(page)

    fun getBySourceAndDestination(sourceId: Int, destinationId: Int) =
        dao.getBySourceAndDestination(sourceId, destinationId)

    fun getNonStopBySourceAndDestination(sourceId: Int, destinationId: Int) =
        dao.getNonStopBySourceAndDestination(sourceId, destinationId)


}