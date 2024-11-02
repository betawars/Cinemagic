package com.example.cinemagic.SearchScreen.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository@Inject constructor(
    private val service: SearchService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var currentQuery:String = ""
    private var currentAuthorization:String = ""
    private var currentSearch: SearchSearchResults? = null
    suspend fun loadResults(query:String,authorization:String): Result<SearchSearchResults?> {


        return if(query == currentQuery){
            Result.success(currentSearch)
        }else{
            currentQuery = query
            currentAuthorization = authorization
            withContext(ioDispatcher) {
                try {
                    val response = service.searchSearch(auth = authorization, query = query)
                    if (response.isSuccessful) {
                        currentSearch = response.body()
                        Result.success(currentSearch)
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