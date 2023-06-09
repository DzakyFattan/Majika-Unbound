package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

data class Menu (
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("sold") val sold: Int?,
    @SerializedName("type") val type: String?
)