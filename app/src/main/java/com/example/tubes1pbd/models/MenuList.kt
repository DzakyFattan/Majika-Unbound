package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

data class MenuList (
    @SerializedName("data") val data: List<Menu>
)