package com.example.flightsearch.ui

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.R
import com.example.flightsearch.adapters.AirportListAdapter
import com.example.flightsearch.databinding.AirportPickerBinding
import com.example.flightsearch.databinding.FragmentHomeBinding
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.repository.AirportRepository
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import com.example.flightsearch.ui.fragments.SearchDialogFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private lateinit var searchFragment: SearchDialogFragment
    private lateinit var adapter: AirportListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var filePickerResolver: ActivityResultLauncher<Array<String>>
    private lateinit var viewModel: AppViewModel
    private var destinationDefault: String = "DEFAULT"


    companion object {
        private const val TAG = "FirstFragment"
        private const val FILE_PICKER = 2

    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        (activity as AppCompatActivity).supportActionBar?.title = "Home"

        viewModel = AppViewModelFactory.getAppViewInstance(this, requireContext())
        searchFragment = SearchDialogFragment.getInstance(viewModel)

        @SuppressLint("SetTextI18n")
        binding.arrivePicker.label.text = "Destination Airport"
        @SuppressLint("SetTextI18n")
        binding.departurePicker.label.text = "Source Country"

        binding.departurePicker.root.setOnClickListener {

            activity?.let {
                searchFragment.setTag(SearchDialogFragment.Companion.Tag.COUNTRY_ONY)
                searchFragment.show(it.supportFragmentManager, "Search_Dialog")
            }
        }
        binding.buttonFirst.setOnClickListener {
            viewModel.selectedOriginCountry.value?.let { it ->
                if (it.isEmpty()) {
                    Toast.makeText(requireContext(), "Select Source", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                viewModel.destinationAirport.value?.let { destination ->
                    if (destination.IATA == destinationDefault) {
                        Toast.makeText(requireContext(), "Select Destination", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }

                    searchPossibleAirports()
                } ?: Toast.makeText(requireContext(), "Select Destination", Toast.LENGTH_SHORT)
                    .show()


            } ?: Toast.makeText(requireContext(), "Select Source", Toast.LENGTH_SHORT).show()
        }

        setUpRecyclerView()
        setObserver()


    }

    @SuppressLint("SetTextI18n")
    fun setAirportPicker(
        view: AirportPickerBinding,
        airport: AirportModel,
        tag: SearchDialogFragment.Companion.Tag
    ) {
        view.apply {
            airportName.text = airport.name
            countryName.text = "${airport.country} - ${airport.city}"
            root.setOnClickListener {
                activity?.let {
                    searchFragment.setTag(tag)
                    searchFragment.show(it.supportFragmentManager, "Search_Dialog")

                }
            }
        }


    }


//    private fun insertToDB(airport: AirportModel) {
//        try {
//            viewModel.registerAirport(airport)
//            Toast.makeText(context, "Added New one", Toast.LENGTH_SHORT).show()
//        } catch (e: java.lang.Exception) {
//            Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AirportListAdapter().also {
            it.setOnItemClickListener { airportModel ->
                viewModel.departureAirport.postValue(airportModel)
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun searchPossibleAirports() {
        val id = viewModel.destinationAirport.value?.airportId ?: 0
        val country = viewModel.selectedOriginCountry.value ?: ""
        viewModel.searchPossibleAirports(id, country)
    }

    private fun setObserver() {


        viewModel.selectedOriginCountry.observe(viewLifecycleOwner) {
            binding.departurePicker.countryName.text = it ?: "Select Country"
        }

        viewModel.destinationAirport.observe(viewLifecycleOwner) {
            setAirportPicker(
                binding.arrivePicker,
                it,
                SearchDialogFragment.Companion.Tag.DESTINATION_SEARCH
            )
        }
        viewModel.possibleAirport.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Result ${it.size}", Toast.LENGTH_SHORT).show()
            adapter.setData(it)
        }

    }


}