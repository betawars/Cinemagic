package com.example.cinemagic.DetailScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.DetailScreen.data.DetailsCastRepository
import com.example.cinemagic.DetailScreen.data.DetailsCastSearchResult
import com.example.cinemagic.DetailScreen.data.DetailsRepository
import com.example.cinemagic.DetailScreen.data.DetailsSearchResults
import com.example.cinemagic.ENUMS.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailsCastViewModel@Inject constructor(
    private val detailsCastRepository: DetailsCastRepository
) : ViewModel() {
    private val _searchCurrentResults = MutableLiveData<DetailsCastSearchResult?>(null)
    val searchCurrentResults: LiveData<DetailsCastSearchResult?> = _searchCurrentResults

    private val _loadingCurrentStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingCurrentStatus: LiveData<LoadingStatus> = _loadingCurrentStatus

    private val _error = MutableLiveData<String>(null)
    val error : LiveData<String?> = _error

    fun loadCurrentDetailCastResults(movieId: Int,auth: String){
        viewModelScope.launch(Dispatchers.IO){
            _loadingCurrentStatus.postValue(LoadingStatus.LOADING)
            val result = detailsCastRepository.loadResults(movieId,auth)
            _searchCurrentResults.postValue(result.getOrNull())
            _error.postValue(result.exceptionOrNull()?.message)
            _loadingCurrentStatus.postValue(when(result.isSuccess){
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            })
        }
    }
}