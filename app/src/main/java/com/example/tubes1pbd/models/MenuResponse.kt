package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

data class MenuResponse (
    @SerializedName("data") val data: List<Menu>
)