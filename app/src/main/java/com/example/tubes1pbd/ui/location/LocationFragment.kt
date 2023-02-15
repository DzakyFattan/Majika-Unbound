package com.example.tubes1pbd.ui.location

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val locationAdapter = LocationAdapter()
        binding.rvLocation.adapter = locationAdapter
        binding.rvLocation.layoutManager = LinearLayoutManager(context)
        locationViewModel.rvLocation.observe(viewLifecycleOwner) {
//            Log.d(TAG, "onCreateView: $it")
            locationAdapter.setLocationsList(it)
        }
        locationViewModel.getLocations()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}