package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("data") val data: List<Location>
)
