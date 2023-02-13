package com.example.tubes1pbd.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.databinding.FragmentLocationBinding
import com.example.tubes1pbd.service.RestApi
import com.example.tubes1pbd.service.RestApiBuilder.getRetrofit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationFragment : Fragment() {
    private val locationItem = ArrayList<Locations>()

    private var _binding: FragmentLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val locationsApi = getRetrofit().create(RestApi::class.java)

        GlobalScope.launch {
            val response = locationsApi.getLocations()
            if (response.isSuccessful) {
                val locations = response.body()
                if (locations != null) {
                    for (location in locations.data) {
                        locationItem.add(location)
//                        Log.d("location", location.toString())
                    }
                }
                withContext(Main) {
                    val recyclerView = binding.rvLocation
                    recyclerView.adapter = LocationAdapter(locationItem)
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}