package com.example.tubes1pbd.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestApiBuilder {

    private const val BASE_URL = "https://majika.reon.my.id/v1/"

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}