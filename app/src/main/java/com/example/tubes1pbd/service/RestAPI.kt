package com.example.tubes1pbd.service

import com.example.tubes1pbd.models.LocationsList
import com.example.tubes1pbd.models.MenuList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface RestAPI {
    // @Headers("Content-Type: application/json")
    @GET("branch")
    fun getLocations(): Call<LocationsList>

    @GET("menu")
    fun getMenu(): Call<MenuList>
}