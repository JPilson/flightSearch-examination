package com.example.flightsearch.repository

import android.util.Log
import androidx.lifecycle.*
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class AppViewModel(private val db: AppDatabase) :
    ViewModel() {
    companion object {
        private const val TAG = "AppViewModel"
    }

    private val airportRepository: AirportRepository by lazy { AirportRepository(db.airportDao()) }
    private val routeRepository: RouteRepository by lazy { RouteRepository(db.routeDao()) }
    private val airlineRepository: AirlineRepository by lazy { AirlineRepository(db.airlineDao()) }
    private val planeRepository: PlaneRepository by lazy { PlaneRepository(db.planeDao()) }


    val airports: MutableLiveData<List<AirportModel>> by lazy { MutableLiveData() }
    val destinationAirport: MutableLiveData<AirportModel> by lazy { MutableLiveData() }
    val departureAirport: MutableLiveData<AirportModel> by lazy { MutableLiveData() }
    val foundRoutes: MutableLiveData<List<TicketModel>> by lazy { MutableLiveData() }
    val possibleTickets: MutableLiveData<List<TicketModel>> by lazy { MutableLiveData() }

    fun flights(): List<String> = TODO("Get List of Flight from AppDatabase ")

    init {
        setDepartureAirport(AirportModel.fromString("1,Select Country of Departure,City,Country,\"DEFAULT\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\""))
        setDestinationAirport(AirportModel.fromString("1,Select Country of Destination,City,Country,\"DEFAULT\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\""))
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

    fun registerRoutes(vararg data: RouteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            routeRepository.register(*data)
        }
    }

    fun registerPlanes(vararg data: PlaneModel) {
        viewModelScope.launch(Dispatchers.IO) {
            planeRepository.register(*data)
        }
    }

    fun registerAirline(vararg data: AirlineModel) {
        viewModelScope.launch(Dispatchers.IO) {
            airlineRepository.register(*data)
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
                    routeRepository.getBySourceAndDestinationWithAirline(sourceId, destinationId)
                        .also {
                            Log.d(TAG, "getRoutesBySourceAndDestination: $it")
//                        foundRoutes.postValue(it)
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