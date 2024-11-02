package com.example.cinemagic.FavoriteScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FavoriteSearchResults(
    @Json(name = "results") val favoriteResultList:List<FavoriteRepo>
)