package com.example.tubes1pbd.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.databinding.FragmentLocationBinding
import kotlin.system.measureTimeMillis

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: LocationAdapter
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(requireActivity())[LocationViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocationAdapter()
        binding.rvLocation.adapter = adapter
        binding.rvLocation.layoutManager = LinearLayoutManager(context)
        locationViewModel.rvLocation.observe(viewLifecycleOwner) {
            // Log.d(TAG, "onCreateView: $it")
            adapter.locationList = it.toMutableList()
        }
        val elapsed = measureTimeMillis {
            locationViewModel.getLocations()
        }
        if (elapsed > 1000 * 30 && (locationViewModel.rvLocation.value == null || locationViewModel.rvLocation.value!!.isEmpty())) {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}