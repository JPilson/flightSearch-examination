package com.example.flightsearch.ui.fragments

import android.app.Dialog
import android.graphics.drawable.Drawable
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

class SearchFragment(private val viewModel: AppViewModel) : DialogFragment() {
    private val TAG = "SearchFragment"
    private var _binding: FragmentSearchBinding? = null
    private lateinit var adapter: AirportListAdapter
    private lateinit var recyclerView: RecyclerView

    private val binding get() = _binding!!

    companion object {
        private var instance: SearchViewModel? = null
        fun getInstance(): SearchFragment {
            if (instance != null)
                return instance as SearchFragment

            instance = SearchViewModel()
            return instance as SearchFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
        adapter = AirportListAdapter()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        viewModel.airports.observe(viewLifecycleOwner) {
            adapter.setData(it)
//            Toast.makeText(requireContext(), " $it", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            val height = android.view.WindowManager.LayoutParams.MATCH_PARENT
            it.window!!.setLayout(width, height)
            it.window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.frame_holder_background, null))

        }
        setSearchView()
        setAdapter()


    }


}