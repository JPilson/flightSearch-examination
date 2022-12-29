package com.example.flightsearch.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.navigation.NavArgs
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.models.RouteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(private val db: AppDatabase) :
    ViewModel() {
    private val airportRepository: AirportRepository by lazy { AirportRepository(db.airportDao()) }
    private val routeRepository: RouteRepository by lazy { RouteRepository(db.routeDao()) }

    val airports: MutableLiveData<List<AirportModel>> by lazy { MutableLiveData() }
    val destinationAirport: MutableLiveData<AirportModel> by lazy { MutableLiveData() }
    val departureAirport: MutableLiveData<AirportModel> by lazy { MutableLiveData() }
    val foundRoutes: MutableLiveData<List<RouteModel>> by lazy { MutableLiveData() }

    fun flights(): List<String> = TODO("Get List of Flight from AppDatabase ")

    init {
        setDepartureAirport(AirportModel.fromString("1,Select Country of Departure,City,Country,\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\""))
        setDestinationAirport(AirportModel.fromString("1,Select Country of Destination,City,Country,\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\""))
    }

    private fun getAirports() {
        viewModelScope.launch(Dispatchers.IO) {
            airports.postValue(airportRepository.getAll(1))
        }
    }

    fun searchAirport(query: String) = viewModelScope.launch(Dispatchers.IO) {
        airports.postValue(airportRepository.search(query))
    }

    fun registerAirport(vararg data: AirportModel) {
        viewModelScope.launch(Dispatchers.IO) {
            airportRepository.registerAirport(*data)
        }
    }

    fun getRoutesBySourceAndDestination(
        sourceId: Int,
        destinationId: Int,
        nonStopOnly: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (nonStopOnly) {
                true -> {
                    routeRepository.getNonStopBySourceAndDestination(sourceId, destinationId).also {
                        foundRoutes.postValue(it)
                    }
                }
                false -> {
                    routeRepository.getBySourceAndDestination(sourceId, destinationId).also {
                        foundRoutes.postValue(it)
                    }
                }
            }

        }
    }

    fun setDestinationAirport(it: AirportModel) = destinationAirport.postValue(it)
    fun setDepartureAirport(it: AirportModel) = departureAirport.postValue(it)


    suspend fun <T> transaction(block: () -> T) = viewModelScope.launch(Dispatchers.IO) {
        block()
    }

}