package com.example.tubes1pbd.models

import com.google.gson.annotations.SerializedName

// val name: String, val popular_food: String, val address: String, val contact_person: String, val phone_number: String, val longitude: Double, val latitude: Double
data class Location(
    @SerializedName("name") val name: String?,
    @SerializedName("popular_food") val popular_food: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("contact_person") val contact_person: String?,
    @SerializedName("phone_number") val phone_number: String?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("latitude") val latitude: Double?
)