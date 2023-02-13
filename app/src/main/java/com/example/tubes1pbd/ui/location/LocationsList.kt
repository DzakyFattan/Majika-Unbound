package com.example.tubes1pbd.ui.location

import com.google.gson.annotations.SerializedName

data class LocationsList(
    @SerializedName("data") val data: List<Locations>
)
