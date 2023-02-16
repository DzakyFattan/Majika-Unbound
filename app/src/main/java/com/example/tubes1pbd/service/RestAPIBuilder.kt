package com.example.tubes1pbd.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestAPIBuilder {

    private const val BASE_URL = "https://majika.reon.my.id/v1/"
    private var restAPI : RestAPI? = null

    fun getClient(): RestAPI {
        if (restAPI == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            restAPI = retrofit.create(RestAPI::class.java)
        }
        return restAPI!!
    }
}