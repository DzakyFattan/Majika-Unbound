package com.example.tubes1pbd.service

import com.example.tubes1pbd.models.LocationsList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface RestApi {
    // @Headers("Content-Type: application/json")
    @GET("branch")
    fun getLocations(): Call<LocationsList>
}