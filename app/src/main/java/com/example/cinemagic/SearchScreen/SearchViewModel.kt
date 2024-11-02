package com.example.cinemagic.SearchScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.FavoriteScreen.data.FavoriteRepository
import com.example.cinemagic.FavoriteScreen.data.FavoriteSearchResults
import com.example.cinemagic.SearchScreen.data.SearchRepository
import com.example.cinemagic.SearchScreen.data.SearchSearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchResults = MutableLiveData<SearchSearchResults?>(null)
    val searchResults: LiveData<SearchSearchResults?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String>(null)
    val error: LiveData<String?> = _error

    fun loadSearchResults(query: String, authorization: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _loadingStatus.postValue(LoadingStatus.LOADING)
            val result = searchRepository.loadResults(query, authorization)
            if (result.getOrNull()?.searchResultList?.size == 0){
                _error.postValue("Please enter valid search query")
            }else{
                _error.postValue(result.exceptionOrNull()?.message)
                _searchResults.postValue(result.getOrNull())
            }
            _loadingStatus.postValue(
                when (result.isSuccess && result.getOrNull()?.searchResultList?.size != 0) {
                    true -> LoadingStatus.SUCCESS
                    false -> LoadingStatus.ERROR
                }
            )

        }
    }
}