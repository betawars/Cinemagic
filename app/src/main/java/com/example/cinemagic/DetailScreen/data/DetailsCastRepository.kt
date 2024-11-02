package com.example.cinemagic.DetailScreen.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsCastRepository @Inject constructor(
    private val service: DetailsCastService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var currentMovieId:Int = 0
    private var currentAuthorization:String = ""
    private var currentDetails: DetailsCastSearchResult? = null
    suspend fun loadResults(movieId:Int,authorization:String): Result<DetailsCastSearchResult?> {


        return if(movieId == currentMovieId){
            Result.success(currentDetails)
        }else{
            currentMovieId = movieId
            currentAuthorization = authorization
            withContext(ioDispatcher) {
                try {
                    val response = service.searchDetails(auth = authorization, detailsMovieId = movieId)
                    if (response.isSuccessful) {
                        currentDetails = response.body()
                        Result.success(currentDetails)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }
}