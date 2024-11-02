package com.example.cinemagic.NotificationScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationRepo(
    @Json(name = "backdrop_path") val notificationImagePath:String?,
    @Json(name = "id") val notificationMovieId:Int?,
    @Json(name = "title") val notificationMovieName:String?,
    @Json(name = "release_date") val notificationMovieReleaseDate:String?,
    @Json(name = "genre_ids") val notificationGenreIds:List<Int>,
    @Json(name = "vote_average") val notificationRatings:Double,

)
@JsonClass(generateAdapter = true)
data class TimeFrame(
    @Json(name = "maximum") val lastDate:String?,
    @Json(name = "minimum") val firstDate:String?,
)