package com.example.cinemagic.HomeScreen.data
import android.app.Application
import android.content.Context
import android.util.Log
import com.example.cinemagic.HomeScreen.data.HomeApiService
import com.example.cinemagic.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: HomeApiService,
    @ApplicationContext private val context: Context, // Use ApplicationContext
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // Utilizing IO Dispatcher for network operations.
) {
    // Cache variables for each movie category
    private var nowShowingCache: HomeSearchResults? = null
    private var popularCache: HomeSearchResults? = null
    private var topRatedCache: HomeSearchResults? = null

    private fun getApiKey(): String = context.getString(R.string.tmdb_api_key)


    suspend fun getNowShowingMovies(apiKey:String): Result<HomeSearchResults> = withContext(ioDispatcher) {
        try {
            Log.d("chech repo",getApiKey())
            if (nowShowingCache == null) { // Check if the cache is null
                val response = apiService.getNowShowingMovies(apiKey)
                if (response.isSuccessful) {
                    nowShowingCache = response.body()
                } else {
                    return@withContext Result.failure(Exception(response.errorBody()?.string()))
                }
            }
            return@withContext Result.success(nowShowingCache!!)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun getPopularMovies(apiKey:String): Result<HomeSearchResults> = withContext(ioDispatcher) {
        try {
            if (popularCache == null) {
                val response = apiService.getPopularMovies(apiKey)
                if (response.isSuccessful) {
                    popularCache = response.body()
                } else {
                    return@withContext Result.failure(Exception(response.errorBody()?.string()))
                }
            }
            return@withContext Result.success(popularCache!!)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun getTopRatedMovies(apiKey:String): Result<HomeSearchResults> = withContext(ioDispatcher) {
        try {
            if (topRatedCache == null) {
                val response = apiService.getTopRatedMovies(apiKey)
                if (response.isSuccessful) {
                    topRatedCache = response.body()
                } else {
                    return@withContext Result.failure(Exception(response.errorBody()?.string()))
                }
            }
            return@withContext Result.success(topRatedCache!!)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}