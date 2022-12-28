package com.example.flightsearch.ui.fragments

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import com.example.flightsearch.R
import com.example.flightsearch.databinding.FragmentSearchBinding

class SearchFragment : DialogFragment() {
    private var _binding: FragmentSearchBinding? = null
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

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dismissBtn.setOnClickListener {
            dialog?.let {
                it.dismiss()
            }
        }

        binding.searchView.requestFocus()
        binding.searchView.setOnClickListener {

        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            val height = android.view.WindowManager.LayoutParams.MATCH_PARENT
            it.window!!.setLayout(width, height)
            it.window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.frame_holder_background, null))

        }


    }


}