package com.example.flightsearch.repository

import androidx.lifecycle.ViewModel
import com.example.flightsearch.db.AppDatabase

class AppViewModel(private val db: AppDatabase):ViewModel() {
    fun flights():List<String> = TODO("Get List of Flight from AppDatabase ")

}