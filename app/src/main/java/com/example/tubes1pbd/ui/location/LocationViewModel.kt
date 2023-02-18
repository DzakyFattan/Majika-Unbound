package com.example.tubes1pbd.ui.location

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1pbd.models.Locations
import com.example.tubes1pbd.models.LocationsList
import com.example.tubes1pbd.service.RestApiBuilder.getClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    private val locationItem = arrayListOf<Locations>()
    private val api = getClient()
    private val _rvLocation = MutableLiveData<List<Locations>>()
    val rvLocation : MutableLiveData<List<Locations>>
        get() = _rvLocation


    fun getLocations() {

        val response = api.getLocations()
        response.enqueue(object : Callback<LocationsList> {
            override fun onResponse(
                call: Call<LocationsList>,
                response: Response<LocationsList>
            ) {
                val locations = response.body()
                val sortedLocations = locations?.data?.sortedBy { it.name }
                Log.d("location", sortedLocations.toString())
                if (sortedLocations != null) {
                    locationItem.addAll(sortedLocations)
                    _rvLocation.postValue(locationItem)
                }
            }

            override fun onFailure(call: Call<LocationsList>, t: Throwable) {
                Log.d("location", t.toString())
            }
        })
    }
}