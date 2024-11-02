package com.example.cinemagic.SearchScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class SearchRepo (
    @Json(name = "id") val searchMovieId:Int,
    @Json(name = "poster_path") val searchImagePath:String?,
    @Json(name = "title") val searchImageTitle:String
): Serializable