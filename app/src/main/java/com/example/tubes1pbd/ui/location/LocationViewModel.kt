package com.example.tubes1pbd.ui.location

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1pbd.models.Locations
import com.example.tubes1pbd.models.LocationsList
import com.example.tubes1pbd.service.RestApi
import com.example.tubes1pbd.service.RestApiBuilder.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    val locationItem = ArrayList<Locations>()
    val rvLocation = MutableLiveData<ArrayList<Locations>>()

    fun getLocations() {
        val locationsApi = getRetrofit().create(RestApi::class.java)
        val response = locationsApi.getLocations()
        response.enqueue(object : Callback<LocationsList> {
            override fun onResponse(
                call: Call<LocationsList>,
                response: Response<LocationsList>
            ) {
                val locations = response.body()
                val sortedLocations = locations?.data?.sortedBy { it.name }
//                Log.d("location", sortedLocations.toString())
                if (sortedLocations != null) {
                    locationItem.addAll(sortedLocations)
                    rvLocation.postValue(locationItem)
                }
            }

            override fun onFailure(call: Call<LocationsList>, t: Throwable) {
                Log.d("location", t.toString())
            }
        })
    }
}