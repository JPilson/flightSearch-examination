package com.example.flightsearch.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.navigation.NavArgs
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(private val airportRepository: AirportRepository) :
    ViewModel() {
    val airports: MutableLiveData<List<AirportModel>> by lazy { MutableLiveData() }
    fun flights(): List<String> = TODO("Get List of Flight from AppDatabase ")

    init {
       getAirports()
    }
    private fun getAirports(){
        viewModelScope.launch(Dispatchers.IO) {
            airports.postValue(airportRepository.getAll(1))
        }
    }
     fun searchAirport(query:String) = viewModelScope.launch(Dispatchers.IO) {
        airports.postValue(airportRepository.search(query))
    }

    fun registerAirport(vararg data: AirportModel) {
        viewModelScope.launch(Dispatchers.IO) {
            airportRepository.registerAirport(*data)
        }
    }

    suspend fun <T> transaction(block: () -> T) = viewModelScope.launch(Dispatchers.IO) {
        block()
    }

}