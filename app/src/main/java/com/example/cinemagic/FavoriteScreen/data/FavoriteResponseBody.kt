package com.example.cinemagic.FavoriteScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteResponseBody(
    @Json(name = "success") val isSuccessful:String,
    @Json(name = "status_code") val statusCode:String,
    @Json(name = "status_message") val statusMessage:String
)
