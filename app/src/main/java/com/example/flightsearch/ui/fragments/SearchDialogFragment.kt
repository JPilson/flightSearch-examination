package com.example.flightsearch.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.R
import com.example.flightsearch.adapters.AirportListAdapter
import com.example.flightsearch.databinding.FragmentSearchBinding
import com.example.flightsearch.repository.AppViewModel

class SearchDialogFragment(private val viewModel: AppViewModel) : DialogFragment() {
    private var _binding: FragmentSearchBinding? = null
    private lateinit var adapter: AirportListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tag: Tag

    private val binding get() = _binding!!

    companion object {
        private const val TAG = "SearchDialogFragment"

        enum class Tag(tag: String) {
            DEPARTURE_SEARCH("departure_search_dialog"),
            DESTINATION_SEARCH("destination_search_dialog")
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

    private fun setSearchView() {
        binding.dismissBtn.setOnClickListener {
            dialog?.dismiss()
        }

        binding.searchView.requestFocus()
        binding.searchView.setOnClickListener {
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                query?.let { viewModel.searchAirport("%${it.lowercase()}%") }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setAdapter() {

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AirportListAdapter().also {
            it.setOnItemClickListener { airPort ->

                when (tag) {
                    Tag.DEPARTURE_SEARCH -> viewModel.setDepartureAirport(airPort)
                    Tag.DESTINATION_SEARCH -> viewModel.setDestinationAirport(airPort)
                }
                dialog?.dismiss()
            }
        }

        viewModel.airports.observe(viewLifecycleOwner) {
            Log.d(TAG, "setAdapter: $it")
            adapter.setData(it)
        }
        recyclerView.adapter = adapter


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


}