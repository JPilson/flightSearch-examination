package com.example.flightsearch.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.flightsearch.R
import com.example.flightsearch.databinding.ActivityMainBinding
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirlineModel
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.models.PlaneModel
import com.example.flightsearch.models.RouteModel
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import kotlinx.coroutines.*
import okio.ByteString.Companion.readByteString
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AppViewModel
    private lateinit var animationView: LottieAnimationView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val TAG = "MainActivity"
        const val SHARED_PREFERENCES = "flight_search_by_jsumbo"
        const val SHARED_PREFERENCES_NON_STOP_STATE = "non_stop"
        const val SHARED_PREFERENCES_IS_DATA_SYNCED = "is_data_synced"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.header.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setUp()

    }

    private fun setUp() {
        viewModel = AppViewModelFactory.getAppViewInstance(this, this)
        animationView = binding.lottieLayer.animationView;
        animationView.setAnimation(R.raw.upload_animation)
        sharedPreferences =
            getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)!!
        when (sharedPreferences.contains(SHARED_PREFERENCES_IS_DATA_SYNCED)) {
            true -> {
//                TODO toast
            }
            false -> {
                syncDataFile()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sync_data -> {
                syncDataFile()
            }
            else -> {}
        }
        return true

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loaderVisibility(visibility: Int, now: Boolean = false) {

        when (visibility) {
            View.VISIBLE -> {
                binding.lottieLayer.animationParentView.visibility = visibility
                animationView.repeatMode = LottieDrawable.RESTART
                animationView.playAnimation()

            }
            else -> {
                if (now) {
                    animationView.pauseAnimation()
                    binding.lottieLayer.animationParentView.visibility = visibility
                    return
                }
                GlobalScope.launch(Dispatchers.Main) {
                    delay(2000)
                    animationView.pauseAnimation()
                    binding.lottieLayer.animationParentView.visibility = visibility
                }


            }
        }

    }


    private fun openDocumentFromResource(resourceId: Int, forEachLine: (String) -> Unit) {
        val openRawResource = resources.openRawResource(resourceId)
        BufferedReader(InputStreamReader(openRawResource)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                forEachLine(line)
                line = reader.readLine()
            }
        }
    }

    private fun syncDataFile() {
        try {
            loaderVisibility(View.VISIBLE)
            val files = listOf(R.raw.airports, R.raw.airlines, R.raw.routes, R.raw.planes)
            files.forEach {
                openDocumentFromResource(it) { line ->
                    when (it) {
                        R.raw.airports -> {
                            viewModel.registerAirport(AirportModel.fromString(line))
                        }
                        R.raw.airlines -> {
                            viewModel.registerAirline(AirlineModel.fromString(line))
                        }
                        R.raw.routes -> {
                            viewModel.registerRoutes(RouteModel.fromString(line))
                        }
                        R.raw.planes -> {
                            viewModel.registerPlanes(PlaneModel.fromString(line))
                        }
                    }
                }
            }

            sharedPreferences.edit().apply {
                putBoolean(SHARED_PREFERENCES_IS_DATA_SYNCED, true)
                apply()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong while updating DB", Toast.LENGTH_SHORT)
                .show()
            sharedPreferences.edit().apply {
                putBoolean(SHARED_PREFERENCES_IS_DATA_SYNCED, false)
                apply()
            }
        } finally {
            loaderVisibility(View.INVISIBLE)
        }
    }

}