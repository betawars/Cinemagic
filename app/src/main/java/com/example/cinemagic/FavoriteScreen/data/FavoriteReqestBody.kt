package com.example.cinemagic.FavoriteScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteRequestBody (
    @Json(name = "media_type") val mediaType:String,
    @Json(name = "media_id") val mediaId:Int,
    @Json(name = "favorite") val isFavorite:Boolean
)