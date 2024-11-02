package com.example.cinemagic.FavoriteScreen.data

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.cinemagic.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository@Inject constructor(
    private val service: FavoriteService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var currentAccountId:String = ""
    private var currentAuthorization:String = ""
    private var currentFavorite:FavoriteSearchResults? = null
    private var currentFavoriteDecision:FavoriteResponseBody? = null

    suspend fun loadResults(accountId:String,authorization:String): Result<FavoriteSearchResults?> {

        return run {
            currentAccountId = accountId
            currentAuthorization = authorization
            withContext(ioDispatcher) {
                try {
                    val response = service.searchFavorite(accountId = accountId, auth = authorization)
                    if (response.isSuccessful) {
                        currentFavorite = response.body()
                        Result.success(currentFavorite)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }

    suspend fun addOrRemoveFavorite(accountId:String,authorization:String,requestBody: FavoriteRequestBody): Result<FavoriteResponseBody?> {

        return run {
            currentAccountId = accountId
            currentAuthorization = authorization
            withContext(ioDispatcher) {
                try {
                    val response = service.addOrRemoveFavorite(accountId = accountId, auth = authorization, requestBody = requestBody)

                    if (response.isSuccessful) {

                        currentFavoriteDecision = response.body()
                        Result.success(currentFavoriteDecision)
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