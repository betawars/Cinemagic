package com.example.cinemagic.DetailScreen.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsService {
    @GET("movie/{movie_id}")
    suspend fun searchDetails(
        @Path("movie_id") detailsMovieId:Int,
        @Header("accept") accept:String = "application/json",
        @Header("Authorization") auth:String
    ): Response<DetailsSearchResults?>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") auth: String
    ): Response<VideosResponse>

}