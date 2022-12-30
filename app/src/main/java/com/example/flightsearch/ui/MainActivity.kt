package com.example.flightsearch.ui

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
import com.example.flightsearch.models.AirlineModel
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.models.PlaneModel
import com.example.flightsearch.models.RouteModel
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AppViewModel
    private lateinit var filePickerResolver: ActivityResultLauncher<Array<String>>
    private lateinit var filePickerAirlineResolver: ActivityResultLauncher<Array<String>>
    private lateinit var filePickerRoutesResolver: ActivityResultLauncher<Array<String>>
    private lateinit var filePickerPlanesResolver: ActivityResultLauncher<Array<String>>
    private lateinit var animationView: LottieAnimationView
    companion object{
        private const val TAG = "MainActivity"
         const val SHARED_PREFERENCES = "flight_search_by_jsumbo"
         const val SHARED_PREFERENCES_NON_STOP_STATE = "non_stop"
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

    fun setUp() {
        viewModel = AppViewModelFactory.getAppViewInstance(this, this)
        animationView = binding.lottieLayer.animationView;
        animationView.setAnimation(R.raw.upload_animation)

        setAirportData()
        setAirlineFilePicker()
        setRoutesFilePicker()
        setPlaneFilePicker()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_import_airport_data -> {
                filePickerResolver.launch(arrayOf("*/*"))
                true
            }
            R.id.action_import_airline_data -> {
                filePickerAirlineResolver.launch(arrayOf("*/*"))
                true
            }
            R.id.action_import_planes_data -> {
                filePickerPlanesResolver.launch(arrayOf("*/*"))
                true
            }
            R.id.action_import_routes_data -> {
                filePickerRoutesResolver.launch(arrayOf("*/*"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    private fun setAirportData() {
        filePickerResolver =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                try {
                    loaderVisibility(View.VISIBLE)
                    openDocument(uri) {
                        viewModel.registerAirport(AirportModel.fromString(it))
                    }
                    Toast.makeText(this, "Data Added ", Toast.LENGTH_SHORT).show()
                    loaderVisibility(View.INVISIBLE)
                } catch (e: Exception) {
                    Log.e(TAG, "saveAirportData: e${e.localizedMessage}\n${e.stackTrace}")
                    Toast.makeText(
                        this,
                        "Something Went Wrong When Adding data",
                        Toast.LENGTH_SHORT
                    ).show()
                    loaderVisibility(View.INVISIBLE, true)
                }
            }
    }

    private fun setAirlineFilePicker() {
        filePickerAirlineResolver =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                try {
                    loaderVisibility(View.VISIBLE)
                    openDocument(uri) {
                        viewModel.registerAirline(AirlineModel.fromString(it))
                    }
                    Toast.makeText(this, "Data Added ", Toast.LENGTH_SHORT).show()
                    loaderVisibility(View.INVISIBLE)
                } catch (e: Exception) {
                    Log.e(TAG, "setAirlineFilePicker: $e\n${e.localizedMessage}\n" +
                            " ${e.stackTrace}", )
                    Toast.makeText(
                        this,
                        "Something Went Wrong When Adding data",
                        Toast.LENGTH_SHORT
                    ).show()
                    loaderVisibility(View.INVISIBLE, true)
                }
            }
    }

    private fun setPlaneFilePicker() {
        filePickerPlanesResolver =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                try {
                    loaderVisibility(View.VISIBLE)
                    openDocument(uri) {
                        viewModel.registerPlanes(PlaneModel.fromString(it))
                    }
                    Toast.makeText(this, "Data Added ", Toast.LENGTH_SHORT).show()
                    loaderVisibility(View.INVISIBLE)
                } catch (e: Exception) {
                    Log.e(TAG, "setPlaneFilePicker:${e.localizedMessage}\n ${e.stackTrace} " )
                    Toast.makeText(
                        this,
                        "Something Went Wrong When Adding data",
                        Toast.LENGTH_SHORT
                    ).show()
                    loaderVisibility(View.INVISIBLE, true)
                }
            }
    }

    private fun setRoutesFilePicker() {
        filePickerRoutesResolver =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                try {
                    loaderVisibility(View.VISIBLE)
                    openDocument(uri) {
                        viewModel.registerRoutes(RouteModel.fromString(it))
                    }
                    Toast.makeText(this, "Data Added ", Toast.LENGTH_SHORT).show()
                    loaderVisibility(View.INVISIBLE)
                } catch (e: Exception) {
                    Log.e(TAG, "setRoutesFilePicker: ${e.localizedMessage}\n ${e.stackTrace}", )
                    Toast.makeText(
                        this,
                        "Something Went Wrong When Adding data",
                        Toast.LENGTH_SHORT
                    ).show()
                    loaderVisibility(View.INVISIBLE, true)
                }
            }
    }

    @Throws(IOException::class)
    fun openDocument(uri: Uri?, forEachLine: (String) -> Unit) {
        if (uri == null) {
            Toast.makeText(this, "Failed to open File", Toast.LENGTH_SHORT).show()
            return
        }
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    forEachLine(line)
                    line = reader.readLine()
                }
            }
        }

    }

}