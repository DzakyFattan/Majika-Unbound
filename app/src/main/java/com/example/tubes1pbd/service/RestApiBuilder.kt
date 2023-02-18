package com.example.tubes1pbd.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestApiBuilder {

    private const val BASE_URL = "https://majika.reon.my.id/v1/"
    private var restApi : RestApi? = null

    fun getClient(): RestApi {
        if (restApi == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            restApi = retrofit.create(RestApi::class.java)
        }
        return restApi!!
    }
}