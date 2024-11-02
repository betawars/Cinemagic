package com.example.cinemagic.HomeScreen.data
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeSearchResults (
    @Json(name = "results") val results: List<HomeRepo>
)