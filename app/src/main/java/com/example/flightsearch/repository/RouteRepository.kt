package com.example.flightsearch.repository

import android.util.Log
import com.example.flightsearch.db.RouteDao
import com.example.flightsearch.models.RouteModel

class RouteRepository(private val dao: RouteDao) {
    companion object {
        private const val TAG = "RouteRepository"
    }
    fun register(vararg it: RouteModel) = try {
        dao.save(*it)
    } catch (e: Exception) {
        Log.d(TAG, "register: ")
    }
    fun getAll(page: Int = 1) = dao.getAll(page)

    fun getBySourceAndDestination(sourceId: Int, destinationId: Int) =
        dao.getBySourceAndDestination(sourceId, destinationId)

    fun getNonStopBySourceAndDestination(sourceId: Int, destinationId: Int) =
        dao.getNonStopBySourceAndDestination(sourceId, destinationId)

    fun getBySourceAndDestinationWithAirline(sourceId: Int, destinationId: Int) =
        dao.getBySourceAndDestinationWithAirline(sourceId, destinationId)



}