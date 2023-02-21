package com.example.tubes1pbd.service

import com.example.tubes1pbd.models.LocationResponse
import com.example.tubes1pbd.models.MenuResponse
import com.example.tubes1pbd.models.DecodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RestApi {
    @GET("branch")
    fun getLocations(): Call<LocationResponse>

    @GET("menu")
    fun getMenu(): Call<MenuResponse>

    @POST("payment/{qrdecode}")
    fun sendQRDecode(@Path("qrdecode") qrdecode: String): Call<DecodeResponse>
}