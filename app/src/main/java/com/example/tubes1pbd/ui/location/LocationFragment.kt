package com.example.tubes1pbd.ui.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.R
import com.example.tubes1pbd.service.RestApi
import com.example.tubes1pbd.service.RestApiBuilder.getRetrofit
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LocationFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<LocationAdapter.Holder>? = null

    private val locationItem = ArrayList<Locations>()
    private val listLocation = arrayOf("a", "b", "c", "d", "e", "f")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationsApi = getRetrofit().create(RestApi::class.java)

        GlobalScope.launch {
            val response = locationsApi.getLocations()
            if (response.isSuccessful) {
                val locations = response.body()
                if (locations != null) {
                    Log.d("LocationFragment", locations.toString())
//                    for (location in locations) {
//                        locationItem.add(location)
//                    }
                }
            }
        }
        location_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = LocationAdapter(locationItem)
        }
    }
}