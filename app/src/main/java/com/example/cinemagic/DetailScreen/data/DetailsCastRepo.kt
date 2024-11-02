package com.example.cinemagic.DetailScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class DetailsCastRepo(
    @Json(name = "id") val detailsCastId:Int?,
    @Json(name = "original_name") val detailsCastName: String?,
    @Json(name = "profile_path") val detailsCastPoster: String?,
    @Json(name = "character") val detailsCastCharacterName: String?
):Serializable
