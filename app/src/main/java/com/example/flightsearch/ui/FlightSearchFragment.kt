package com.example.flightsearch.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.adapters.RouteListAdapter
import com.example.flightsearch.databinding.FragmentFlightSearchBinding
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import com.example.flightsearch.ui.fragments.SearchDialogFragment
import kotlinx.coroutines.*
import kotlin.math.log

class FlightSearchFragment : Fragment() {

    private var _binding: FragmentFlightSearchBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: AppViewModel
    private lateinit var routesAdapter: RouteListAdapter
    private lateinit var routesRecyclerView: RecyclerView
    private lateinit var searchFragment:SearchDialogFragment
    private var sourceId: Int = 0
    private var destinationId: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val TAG = "FlightSearchFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlightSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.let {
            it.title = "Routes & Tickets"
        }
        setUp()
    }

    private fun setUp() {
        sharedPreferences =
            activity?.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)!!

        viewModel = AppViewModelFactory.getAppViewInstance(this, requireContext())
        searchFragment = SearchDialogFragment.getInstance(viewModel)
        setupRecyclerView()


        when (sharedPreferences.contains(MainActivity.SHARED_PREFERENCES_NON_STOP_STATE)) {
            true -> binding.noStopFlightSwitch.isChecked =
                sharedPreferences.getBoolean(MainActivity.SHARED_PREFERENCES_NON_STOP_STATE, false)
            false -> saveNoStopSwitchState()

        }

        binding.noStopFlightSwitch.setOnCheckedChangeListener { _, _ ->
            saveNoStopSwitchState()
        }

        binding.destinationSelection.setOnClickListener{
            searchFragment.setTag(SearchDialogFragment.Companion.Tag.DESTINATION_SEARCH)
            showDialog()

        }

        binding.departureSelection.setOnClickListener{
            searchFragment.setTag(SearchDialogFragment.Companion.Tag.COUNTRY_ONY)
            showDialog()
        }

    }
    private fun showDialog(){
        activity?.let {
            searchFragment.show(it.supportFragmentManager, "Search_Dialog")

        }
    }

    private fun saveNoStopSwitchState() {
        sharedPreferences.edit().apply {
            putBoolean(
                MainActivity.SHARED_PREFERENCES_NON_STOP_STATE,
                binding.noStopFlightSwitch.isChecked
            )
            apply()
        }
    }

    private fun searchRoutes() {
//        viewModel.getRoutesBySourceAndDestination(
//            sourceId,
//            destinationId,
//            binding.noStopFlightSwitch.isChecked
//        )
        viewModel.getFlights(destinationId,viewModel.selectedOriginCountry.value!!)
    }

    private fun setupRecyclerView() {
        routesAdapter = RouteListAdapter().also {
            it.setOnItemClickListener { ticket -> showAirport(ticket.route.sourceAirportId.toInt())}
        }

        routesRecyclerView = binding.recyclerView
        routesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.foundRoutes.observe(viewLifecycleOwner) {
            routesAdapter.setData(it)
        }
        routesRecyclerView.adapter = routesAdapter
        subscribeToViewModelChanges()
    }
     private  fun showAirport(id:Int){
        runBlocking {
            val airportById = viewModel.getAirportById(id)
            if(airportById == null){
                Toast.makeText(
                    requireContext(),
                    "Airport Not Found $id",
                    Toast.LENGTH_SHORT
                ).show()
                return@runBlocking
            }
            Toast.makeText(
                requireContext(),
                "Opening Map to: ${airportById.name} in ${airportById.country} ",
                Toast.LENGTH_SHORT
            ).show()
            openMaps(airportById)
        }
    }


    private fun openMaps(airport:AirportModel) {
        val location = "geo:${airport.latitude},${airport.longitude}?z=15f"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(location))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun subscribeToViewModelChanges(){
        viewModel.apply {
            selectedOriginCountry.observe(viewLifecycleOwner){
                binding.departureSelection.text = it
                searchRoutes()
            }

            destinationAirport.observe(viewLifecycleOwner) {
                binding.destinationSelection.text = it.name
                destinationId = it.airportId
                searchRoutes()

            }
        }
    }
}