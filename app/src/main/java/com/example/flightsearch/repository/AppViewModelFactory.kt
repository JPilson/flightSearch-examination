package com.example.flightsearch.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearch.db.AppDatabase

class AppViewModelFactory(private val db:AppDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return  AppViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown View Model")
//        return super.create(modelClass)
    }
}