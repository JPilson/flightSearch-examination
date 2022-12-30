package com.example.flightsearch.models

import androidx.room.Embedded
import androidx.room.Relation

data class TicketModel(
    @Embedded val route: RouteModel,

    @Relation(
        parentColumn = "airlineId",
        entityColumn = "airlineId"
    )
    val airline: AirlineModel,
)