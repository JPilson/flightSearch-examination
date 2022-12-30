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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.adapters.RouteListAdapter
import com.example.flightsearch.databinding.FragmentFlightSearchBinding
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import kotlin.math.log

class FlightSearchFragment : Fragment() {

    private var _binding: FragmentFlightSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: AppViewModel
    private lateinit var routesAdapter: RouteListAdapter
    private lateinit var routesRecyclerView: RecyclerView
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
        setUp()
//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }

    private fun setUp() {
        sharedPreferences =
            activity?.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)!!

        viewModel =
            AppViewModelFactory.getAppViewInstance(this, requireContext())
        setupRecyclerView()
        binding.swap.setOnClickListener {
            viewModel.swapLocations()
        }

        when (sharedPreferences.contains(MainActivity.SHARED_PREFERENCES_NON_STOP_STATE)) {
            true -> binding.noStopFlightSwitch.isChecked =
                sharedPreferences.getBoolean(MainActivity.SHARED_PREFERENCES_NON_STOP_STATE, false)
            false -> saveNoStopSwitchState()

        }
        binding.noStopFlightSwitch.setOnCheckedChangeListener { _, _ ->
            saveNoStopSwitchState()
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
        viewModel.getRoutesBySourceAndDestination(
            sourceId,
            destinationId,
            binding.noStopFlightSwitch.isChecked
        )
    }

    private fun setupRecyclerView() {
        routesAdapter = RouteListAdapter().also {
            it.setOnItemClickListener { _ ->

                Toast.makeText(
                    requireContext(),
                    "Opening Map to: ${viewModel.departureAirport.value?.name} in ${viewModel.departureAirport.value?.country} ",
                    Toast.LENGTH_SHORT
                ).show()
                openMaps()
            }
        }

        routesRecyclerView = binding.recyclerView
        routesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.foundRoutes.observe(viewLifecycleOwner) {
            routesAdapter.setData(it)

        }
        routesRecyclerView.adapter = routesAdapter
        viewModel.apply {
            departureAirport.observe(viewLifecycleOwner) {
                binding.departureSelection.text = it.name
                sourceId = it.airportId
                searchRoutes()
            }
            destinationAirport.observe(viewLifecycleOwner) {
                binding.destinationSelection.text = it.name
                destinationId = it.airportId
                searchRoutes()

            }
        }


    }


    private fun openMaps() {
        val source = viewModel.departureAirport.value!!
        val location = "geo:${source.latitude},${source.longitude}?z=15f"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(location))
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}