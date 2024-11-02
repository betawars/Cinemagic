package com.example.cinemagic.HomeScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class HomeRepo (
@Json(name = "id") val id: Int,
@Json(name = "title") val title: String,
@Json(name = "poster_path") val posterPath: String,
@Json(name = "vote_average") val rating: Double
)