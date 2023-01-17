package com.example.flightsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.R
import com.example.flightsearch.adapters.AirportListAdapter
import com.example.flightsearch.adapters.CountryListAdapter
import com.example.flightsearch.databinding.FragmentSearchBinding
import com.example.flightsearch.repository.AppViewModel

class SearchDialogFragment(private val viewModel: AppViewModel) : DialogFragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var adapter: AirportListAdapter
    private lateinit var countryListAdapter: CountryListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tag: Tag

    private val binding get() = _binding!!

    companion object {
        private const val TAG = "SearchDialogFragment"
        private var instance:SearchDialogFragment? = null
        public fun getInstance(viewModel:AppViewModel):SearchDialogFragment {
            if(instance == null)
                instance = SearchDialogFragment(viewModel)
            return instance!!
        }

        enum class Tag(tag: String) {
            DEPARTURE_SEARCH("departure_search_dialog"),
            DESTINATION_SEARCH("destination_search_dialog"),
            COUNTRY_ONY("country")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    fun setTag(it: Tag) {
        tag = it
    }

    private fun dismissDialog() {
        viewModel.airports.postValue(listOf())
        binding.searchView.setQuery("", false)
        dialog?.dismiss()
    }

    private fun setSearchView() {
        binding.dismissBtn.setOnClickListener {
            dismissDialog()
        }

        binding.searchView.requestFocus()
        binding.searchView.setOnClickListener {
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isEmpty() == true) {
                    return false
                }
                binding.searchView.clearFocus()
                query?.let { search(it.lowercase()) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    return false
                }
                newText?.let { search(it.lowercase()) }
                return true
            }
        })
    }

    private fun search(query: String) {
        when (tag) {
            Tag.DEPARTURE_SEARCH -> viewModel.searchAirport("%${query}%")
            Tag.DESTINATION_SEARCH -> viewModel.searchAirport("%${query}%")
            Tag.COUNTRY_ONY -> viewModel.searchCountries("%${query}%")
        }
    }

    private fun setAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        when (tag) {
            Tag.DEPARTURE_SEARCH -> setAirportAdapter()
            Tag.DESTINATION_SEARCH -> setAirportAdapter()
            Tag.COUNTRY_ONY -> setCountryListAdapter()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            val height = android.view.WindowManager.LayoutParams.MATCH_PARENT
            it.window!!.setLayout(width, height)
            it.window!!.setBackgroundDrawable(
                resources.getDrawable(R.drawable.frame_holder_background, null)
            )

        }
        setSearchView()
        setAdapter()
    }

    private fun setCountryListAdapter() {
        countryListAdapter = CountryListAdapter().also {
            it.setOnItemClickListener { country ->
                viewModel.selectedOriginCountry.postValue(country)
                dismissDialog()
            }
        }

        viewModel.countriesResult.observe(viewLifecycleOwner) {
            countryListAdapter.setData(it)
        }
        recyclerView.adapter = countryListAdapter
    }

    private fun setAirportAdapter() {
        adapter = AirportListAdapter().also {
            it.setOnItemClickListener { airPort ->
                when (tag) {
                    Tag.DEPARTURE_SEARCH -> viewModel.setDepartureAirport(airPort)
                    Tag.DESTINATION_SEARCH -> viewModel.setDestinationAirport(airPort)
                    else -> {}
                }
                dismissDialog()
            }
        }

        viewModel.airports.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        recyclerView.adapter = adapter
    }


}