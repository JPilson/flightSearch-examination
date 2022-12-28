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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.flightsearch.R
import com.example.flightsearch.databinding.AirportPickerBinding
import com.example.flightsearch.databinding.FragmentHomeBinding
import com.example.flightsearch.db.AppDatabase
import com.example.flightsearch.models.AirportModel
import com.example.flightsearch.repository.AirportRepository
import com.example.flightsearch.repository.AppViewModel
import com.example.flightsearch.repository.AppViewModelFactory
import com.example.flightsearch.ui.fragments.SearchFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private lateinit var searchFragment: SearchFragment
    private lateinit var contentResolver: ContentResolver
    private lateinit var filePickerResolver: ActivityResultLauncher<Array<String>>
    private lateinit var viewModel: AppViewModel

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()


    }

    private fun setUp() {
        val appViewModelFactory =
            AppViewModelFactory(AirportRepository(AppDatabase.getDatabase(requireContext())))
        viewModel = ViewModelProvider(this, appViewModelFactory)[AppViewModel::class.java]
        contentResolver = context?.contentResolver!!
        filePickerResolver = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            openDocument(it)
        }
        searchFragment = SearchFragment(viewModel)

        @SuppressLint("SetTextI18n")
        binding.arrivePicker.label.text = "TO"
        viewModel.departureAirport.observe(viewLifecycleOwner) {
            setAirportPicker(
                binding.departurePicker,
                it,
                SearchFragment.Companion.Tag.DEPARTURE_SEARCH
            )
        }
        viewModel.destinationAirport.observe(viewLifecycleOwner) {
            setAirportPicker(
                binding.arrivePicker,
                it,
                SearchFragment.Companion.Tag.DESTINATION_SEARCH
            )
        }
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            insertToDB(AirportModel.fromString("1,\"Goroka Airport\",\"Goroka\",\"Papua New Guinea\",\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\""))
        }


    }

    @SuppressLint("SetTextI18n")
    fun setAirportPicker(
        view: AirportPickerBinding,
        airport: AirportModel,
        tag: SearchFragment.Companion.Tag
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


    private fun insertToDB(airport: AirportModel) {
        try {
            viewModel.registerAirport(airport)
            Toast.makeText(context, "Added New one", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openDocumentPicker() {
        filePickerResolver.launch(arrayOf("*/*"))
    }


    @Throws(IOException::class)
    fun openDocument(uri: Uri?) {
        if (uri == null) {
            Toast.makeText(context, "Failed to open File", Toast.LENGTH_SHORT).show()
            return
        }
        val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    Log.d(TAG, "openDocument: $line")
                    line = reader.readLine()
                }
            }
        }

    }

}