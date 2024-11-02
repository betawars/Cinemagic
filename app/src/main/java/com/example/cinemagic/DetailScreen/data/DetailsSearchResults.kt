package com.example.cinemagic.DetailScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class DetailsSearchResults (
    @Json(name="backdrop_path") val detailsBackgroundImagePath:String?,
    @Json(name="title") val detailsMovieTitle:String,
    @Json(name="vote_average") val detailsMovieRating:Double,
    @Json(name="genres") val detailsGenresList:List<GenreList>,
    @Json(name="runtime") val detailsRuntime:Double,
    @Json(name="spoken_languages") val detailsLanguageList:List<LanguageList>,
    @Json(name="adult") val detailsAdultRating:Boolean,
    @Json(name="overview") val detailsMovieOverview:String

):Serializable
@JsonClass(generateAdapter = true)
class GenreList(
    @Json(name="id") val detailsGenreId:Double,
    @Json(name="name") val detailsGenreName:String,
):Serializable

@JsonClass(generateAdapter = true)
class LanguageList(
    @Json(name="english_name") val detailLanguageEnglishName:String,
    @Json(name="iso_639_1") val detailsLanguageIso:String,
    @Json(name="name") val detailsLanguageName:String,
):Serializable