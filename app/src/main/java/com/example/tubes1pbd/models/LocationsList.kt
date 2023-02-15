package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

data class LocationsList(
    @SerializedName("data") val data: List<Locations>
)
