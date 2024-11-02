package com.example.cinemagic.DetailScreen.data

import com.example.cinemagic.FavoriteScreen.data.FavoriteRepo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DetailsCastSearchResult (
    @Json(name = "cast") val detailsCastResults:List<DetailsCastRepo>
)

