package com.example.flightsearch.ui

import android.os.Bundle
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

class FlightSearchFragment : Fragment() {

    private var _binding: FragmentFlightSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: AppViewModel
    private lateinit var routesAdapter: RouteListAdapter
    private lateinit var routesRecyclerView: RecyclerView

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
            AppViewModelFactory.getAppViewInstance(this,requireContext())
        viewModel.apply {
            departureAirport.observe(viewLifecycleOwner) {
                binding.departureSelection.text = it.name
            }
            destinationAirport.observe(viewLifecycleOwner) {
                binding.destinationSelection.text = it.name
            }
        }



        setupRecyclerView()
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
            routesAdapter.setData(it)
        }
        routesRecyclerView.adapter = routesAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}