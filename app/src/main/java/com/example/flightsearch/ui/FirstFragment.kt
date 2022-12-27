package com.example.flightsearch.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flightsearch.databinding.FragmentFirstBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val TAG = "FirstFragment"
    private val FILE_PICKER = 2
    private var _binding: FragmentFirstBinding? = null
     lateinit var contentResolver:ContentResolver


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
        binding.buttonFirst.setOnClickListener {
            openDocumentPicker()
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun openDocumentPicker(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
//            putExtra(DocumentsContract.EXTRA_INITIAL_URI)

        }
        startActivityForResult(intent,FILE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FILE_PICKER && resultCode == Activity.RESULT_OK){
           data?.also { Log.d(TAG, "onActivityResult: ${it.toString()} ") }
            data?.data?.also {
               contentResolver.takePersistableUriPermission(it,Intent.FLAG_GRANT_READ_URI_PERMISSION)
                openDocument(it)
            }
           
           
        }
    }
    @Throws(IOException::class)
    fun openDocument (uri:Uri){
       val stringBuilder = StringBuilder()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader -> 
                var line:String? = reader.readLine()
                while (line !=null){
                    Log.d(TAG, "openDocument: $line")
                    line = reader.readLine()
                }
            }
        }
        
    }

}