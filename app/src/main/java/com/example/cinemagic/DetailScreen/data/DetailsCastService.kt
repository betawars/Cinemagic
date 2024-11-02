package com.example.cinemagic.DetailScreen.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DetailsCastService {

    @GET("movie/{movie_id}/credits")
    suspend fun searchDetails(
        @Path("movie_id") detailsMovieId:Int,
        @Header("accept") accept:String = "application/json",
        @Header("Authorization") auth:String
    ): Response<DetailsCastSearchResult?>

}