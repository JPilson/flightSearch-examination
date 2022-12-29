package com.example.flightsearch.ui

import android.net.Uri
import android.os.Bundle
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
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: AppViewModel
    private lateinit var filePickerResolver: ActivityResultLauncher<Array<String>>
    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        setUp()

    }

    fun setUp() {
        viewModel = AppViewModelFactory.getAppViewInstance(this, this)
        animationView = binding.lottieLayer.animationView;
        animationView.setAnimation(R.raw.upload_animation)

        setAirportData()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun loaderVisibility(visibility: Int) {
        binding.lottieLayer.animationParentView.visibility = visibility
        when (visibility) {
            View.VISIBLE -> {
                animationView.repeatMode = LottieDrawable.RESTART
                animationView.playAnimation()

            }
            else -> {
                animationView.pauseAnimation()
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
                } catch (e: Exception) {
                    Log.e(TAG, "saveAirportData: e${e.localizedMessage}\n${e.stackTrace}")
                    Toast.makeText(
                        this,
                        "Something Went Wrong When Adding data",
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    loaderVisibility(View.INVISIBLE)
                }
            }
    }

    private fun saveFlightData() {

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