package com.example.flightsearch.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearch.db.AppDatabase

class AppViewModelFactory(val repository: AirportRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")

            return  AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model")
//        return super.create(modelClass)
    }
}