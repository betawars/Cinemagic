package com.example.cinemagic.SearchScreen.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {
    @GET("search/movie")
    suspend fun searchSearch(
        @Header("accept") accept:String = "application/json",
        @Header("Authorization") auth:String,
        @Query("query") query:String
    ): Response<SearchSearchResults?>
}