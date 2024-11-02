package com.example.cinemagic.FavoriteScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.FavoriteScreen.data.FavoriteRepo
import com.example.cinemagic.FavoriteScreen.data.FavoriteRepository
import com.example.cinemagic.FavoriteScreen.data.FavoriteRequestBody
import com.example.cinemagic.FavoriteScreen.data.FavoriteResponseBody
import com.example.cinemagic.FavoriteScreen.data.FavoriteSearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _searchResults = MutableLiveData<FavoriteSearchResults?>(null)
    val searchResults: LiveData<FavoriteSearchResults?> = _searchResults

    private val _searchResultsAddOrRemove = MutableLiveData<FavoriteResponseBody?>(null)
    val searchResultsAddOrRemove: LiveData<FavoriteResponseBody?> = _searchResultsAddOrRemove

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String>(null)
    val error : LiveData<String?> = _error



    private val genreIdToNameMap = mapOf(28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )


    fun updateFavoriteGenresWithNames(favorites: List<FavoriteRepo>): List<FavoriteRepo> {
        return favorites.map { favorite ->
            // Copy each favorite item with updated genre names
            favorite.copy(
                genreNames = favorite.genreIds.mapNotNull { id ->
                    genreIdToNameMap[id.toInt()] // Ensure this map exists and is accessible
                }
            )
        }
    }

    fun loadSearchResults(accountId:String,authorization:String) {
        viewModelScope.launch(Dispatchers.IO){
            _loadingStatus.postValue(LoadingStatus.LOADING)
            val result = favoriteRepository.loadResults(accountId,authorization)
            val searchResults = result.getOrNull()
            try {
               // val result = favoriteRepository.loadResults(accountId, authorization)
                if (result.isSuccess) {
                    val searchResults = result.getOrNull()
                    if (searchResults?.favoriteResultList.isNullOrEmpty()) {
                        // Handle empty results but consider as SUCCESS
                        _error.postValue("") // Consider not setting this as an error.
                        _searchResults.postValue(searchResults) // Post empty results to trigger observer.
                    } else {
                        _searchResults.postValue(searchResults)
                    }
                    _loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else {
                    _error.postValue(result.exceptionOrNull()?.message ?: "Unknown error")
                    _loadingStatus.postValue(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unknown error occurred")
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun addOrRemoveFavorite(accountId:String,authorization:String,requestBody: FavoriteRequestBody) {
        viewModelScope.launch(Dispatchers.IO){
            _loadingStatus.postValue(LoadingStatus.LOADING)
            val result = favoriteRepository.addOrRemoveFavorite(accountId,authorization,requestBody)
            if (result.isSuccess) {
                _searchResultsAddOrRemove.postValue(result.getOrNull())
                // Refresh the list of favorite movies after a successful add or remove operation.
                loadSearchResults(accountId, authorization)
            } else {
                _error.postValue(result.exceptionOrNull()?.message)
            }
            _loadingStatus.postValue(if (result.isSuccess) LoadingStatus.SUCCESS else LoadingStatus.ERROR)
        }
    }

}