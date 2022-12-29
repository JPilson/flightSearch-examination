package com.example.flightsearch.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.flightsearch.db.AppDatabase

class AppViewModelFactory(val parent: Context) : ViewModelProvider.Factory {
    companion object {
        private var appViewModel: AppViewModel? = null
        fun getAppViewInstance(owner: ViewModelStoreOwner, context: Context): AppViewModel {
            return appViewModel ?: synchronized(this) {
                val factory = AppViewModelFactory(context)
                appViewModel = ViewModelProvider(owner, factory)[AppViewModel::class.java]
                return appViewModel!!
            }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return AppViewModel(AppDatabase.getDatabase(parent)) as T
        }
        throw IllegalArgumentException("Unknown View Model")
//        return super.create(modelClass)
    }
}