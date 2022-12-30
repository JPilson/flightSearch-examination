package com.example.flightsearch.db

import androidx.room.TypeConverter
import com.example.flightsearch.models.AirlineModel
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.models.RouteModel
import com.example.flightsearch.models.TicketModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun routeModelToString(it: RouteModel): String {
        val ticket = object : TypeToken<RouteModel>() {}.type
        return Gson().toJson(it, ticket)
    }

    @TypeConverter
    fun airlineModelToString(it: AirportModel): String {
        val ticket = object : TypeToken<AirportModel>() {}.type
        return Gson().toJson(it, ticket)
    }

    @TypeConverter
    fun stringToRouteDataClass(it: String): RouteModel {
        val type = object : TypeToken<RouteModel>() {}.type
        return Gson().fromJson(it, type)
    }

    @TypeConverter
    fun stringToAirlineDataClass(it: String): AirlineModel {
        val type = object : TypeToken<AirlineModel>() {}.type
        return Gson().fromJson(it, type)
    }
}