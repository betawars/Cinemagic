package com.example.cinemagic.FavoriteScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class FavoriteRepo(
    @Json(name = "title") val favoriteTile:String,
    @Json(name = "vote_average") val ratings: Double,
    @Json(name = "genre_ids") val genreIds: List<Double>,
    @Json(name = "id") val movieId: Int,
    @Json(name = "release_date") val releaseDate:String,
    @Json(name = "poster_path") val movieImagePath:String,
    var genreNames: List<String> = listOf(),
):Serializable
