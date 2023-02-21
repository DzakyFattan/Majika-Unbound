package com.example.tubes1pbd.ui.location

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1pbd.models.Location
import com.example.tubes1pbd.models.LocationResponse
import com.example.tubes1pbd.service.RestApiBuilder.getClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    private val locationItem = arrayListOf<Location>()
    private val api = getClient()
    private val _rvLocation = MutableLiveData<List<Location>>()
    val rvLocation : MutableLiveData<List<Location>>
        get() = _rvLocation


    fun getLocations() {

        val response = api.getLocations()
        response.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(
                call: Call<LocationResponse>,
                response: Response<LocationResponse>
            ) {
                val locations = response.body()
                val sortedLocations = locations?.data?.sortedBy { it.name }
                Log.d("location", sortedLocations.toString())
                if (sortedLocations != null) {
                    locationItem.addAll(sortedLocations)
                    _rvLocation.postValue(locationItem)
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                Log.d("location", t.toString())
            }
        })
    }
}