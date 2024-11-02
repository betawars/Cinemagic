package com.example.cinemagic.NotificationScreen.data

import com.example.cinemagic.SearchScreen.data.SearchSearchResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NotificationService {
    @GET("movie/upcoming")
    suspend fun notificationSearch(
        @Header("accept") accept:String = "application/json",
        @Header("Authorization") auth:String
    ): Response<NotificationSearchResults?>
}