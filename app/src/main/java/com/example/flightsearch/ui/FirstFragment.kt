package com.example.flightsearch.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
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
import com.example.flightsearch.databinding.FragmentFirstBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {


    private var _binding: FragmentFirstBinding? = null
    lateinit var contentResolver: ContentResolver
    lateinit var filePickerResolver: ActivityResultLauncher<Array<String>>

    //    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    companion object {
        private const val TAG = "FirstFragment"
        private const val FILE_PICKER = 2
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentResolver = context?.contentResolver!!

        filePickerResolver = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            openDocument(it)
        }

        binding.buttonFirst.setOnClickListener {
            openDocumentPicker()
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
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
        if(uri == null){
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