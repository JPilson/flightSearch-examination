package com.example.flightsearch.ui

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

        viewModel =
            AppViewModelFactory.getAppViewInstance(this, requireContext())



        setupRecyclerView()
    }

    private fun searchRoutes() {
        Log.d(TAG, "searchRoutes : $sourceId $destinationId")
        viewModel.getRoutesBySourceAndDestination(sourceId, destinationId)
    }

    private fun setupRecyclerView() {
        routesAdapter = RouteListAdapter().also {
            it.setOnItemClickListener { route ->
//                TODO: GO ot google maps
                Toast.makeText(requireContext(), "GO to Maps", Toast.LENGTH_SHORT).show()
            }
        }

        routesRecyclerView = binding.recyclerView
        routesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.foundRoutes.observe(viewLifecycleOwner) {
            Log.d(TAG, "setupRecyclerView: $it")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}