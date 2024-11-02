package com.example.cinemagic.FavoriteScreen.data

import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteService {
    @GET("account/{account_id}/favorite/movies")
    suspend fun searchFavorite(
        @Path("account_id") accountId:String,
        @Header("accept") accept:String = "application/json",
        @Header("Authorization") auth:String
        ): Response<FavoriteSearchResults?>

    @POST("account/{account_id}/favorite")
    suspend fun addOrRemoveFavorite(
        @Path("account_id") accountId:String,
        @Header("accept") accept:String = "application/json",
        @Header("content-type") contentType:String = "application/json",
        @Header("Authorization") auth:String,
        @Body requestBody : FavoriteRequestBody
    ):Response<FavoriteResponseBody?>

}