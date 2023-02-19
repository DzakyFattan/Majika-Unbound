package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("quantity") val quantity: Int
)
