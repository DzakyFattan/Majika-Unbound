package com.example.tubes1pbd.service

import com.example.tubes1pbd.models.LocationsList
import com.example.tubes1pbd.models.MenuList
import retrofit2.Call
import retrofit2.http.GET

interface RestApi {
    @GET("branch")
    fun getLocations(): Call<LocationsList>

    @GET("menu")
    fun getMenu(): Call<MenuList>
}