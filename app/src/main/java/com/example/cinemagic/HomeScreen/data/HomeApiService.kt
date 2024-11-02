package com.example.cinemagic.HomeScreen.data
import com.example.cinemagic.HomeScreen.data.HomeSearchResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {

    @GET("movie/now_playing")
    suspend fun getNowShowingMovies(@Query("api_key") apiKey: String): Response<HomeSearchResults>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): Response<HomeSearchResults>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String): Response<HomeSearchResults>
}

