package com.example.cinemagic.DetailScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VideosResponse(
    @Json(name = "results") val results: List<Video>
)

@JsonClass(generateAdapter = true)
class Video(
    @Json(name = "key") val key: String,
    @Json(name = "type") val type: String // Use this to filter for "Trailer"
)

