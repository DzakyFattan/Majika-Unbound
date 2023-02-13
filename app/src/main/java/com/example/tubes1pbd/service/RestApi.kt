package com.example.tubes1pbd.service

import com.example.tubes1pbd.ui.location.Locations
import retrofit2.Response
import retrofit2.http.GET

interface RestApi {
    // @Headers("Content-Type: application/json")
    @GET("branch")
    suspend fun getLocations(): Response<ArrayList<Locations>>
}