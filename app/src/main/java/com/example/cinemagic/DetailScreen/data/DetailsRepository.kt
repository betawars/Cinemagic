package com.example.cinemagic.DetailScreen.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val service: DetailsService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var currentMovieId: Int = 0
    private var currentAuthorization: String = ""
    private var currentDetails: DetailsSearchResults? = null

    suspend fun loadResults(movieId: Int, authorization: String): Result<DetailsSearchResults?> {
        Log.d("DetailsRepository", "Loading results for movie ID: $movieId")
        return if (movieId == currentMovieId) {
            Log.d("DetailsRepository", "Returning cached details for movie ID: $movieId")
            Result.success(currentDetails)
        } else {
            currentMovieId = movieId
            currentAuthorization = authorization
            withContext(ioDispatcher) {
                try {
                    Log.d("DetailsRepository", "Fetching details from network for movie ID: $movieId")
                    val response = service.searchDetails(auth = authorization, detailsMovieId = movieId)
                    if (response.isSuccessful) {
                        currentDetails = response.body()
                        Log.d("DetailsRepository", "Successfully fetched details for movie ID: $movieId")
                        Result.success(currentDetails)
                    } else {
                        Log.e("DetailsRepository", "Failed to fetch details for movie ID: $movieId, Error: ${response.errorBody()?.string()}")
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Log.e("DetailsRepository", "Exception while fetching details for movie ID: $movieId, Error: ${e.message}")
                    Result.failure(e)
                }
            }
        }
    }

    suspend fun getMovieTrailerKey(movieId: Int, auth: String): Result<String> {
        Log.d("DetailsRepository", "Attempting to fetch trailer key for movie ID: $movieId")
        return withContext(ioDispatcher) {
            try {
                val response = service.getMovieVideos(movieId, auth)
                if (response.isSuccessful) {
                    val videoKey = response.body()?.results?.firstOrNull { it.type == "Trailer" }?.key
                    if (videoKey != null) {
                        Log.d("DetailsRepository", "Successfully fetched trailer key for movie ID: $movieId, Key: $videoKey")
                        Result.success(videoKey)
                    } else {
                        Log.e("DetailsRepository", "Trailer key not available for movie ID: $movieId")
                        Result.failure(Throwable("No trailer available"))
                    }
                } else {
                    Log.e("DetailsRepository", "Error fetching trailer for movie ID: $movieId, Error: ${response.errorBody()?.string()}")
                    Result.failure(Throwable("Error fetching trailer: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Log.e("DetailsRepository", "Exception while fetching trailer for movie ID: $movieId, Error: ${e.message}")
                Result.failure(e)
            }
        }
    }
}
