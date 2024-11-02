package com.example.cinemagic.DetailScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.DetailScreen.data.DetailsRepository
import com.example.cinemagic.DetailScreen.data.DetailsSearchResults
import com.example.cinemagic.ENUMS.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository
) : ViewModel() {
    private val _searchCurrentResults = MutableLiveData<DetailsSearchResults?>(null)
    val searchCurrentResults: LiveData<DetailsSearchResults?> = _searchCurrentResults
    private val _trailerKey = MutableLiveData<String?>()
    val trailerKey: LiveData<String?> = _trailerKey


    private val _loadingCurrentStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingCurrentStatus: LiveData<LoadingStatus> = _loadingCurrentStatus

    private val _error = MutableLiveData<String>(null)
    val error : LiveData<String?> = _error

    fun loadCurrentDetailResults(movieId: Int,auth: String){
        viewModelScope.launch(Dispatchers.IO){
            _loadingCurrentStatus.postValue(LoadingStatus.LOADING)
            val result = detailsRepository.loadResults(movieId,auth)
            _searchCurrentResults.postValue(result.getOrNull())
            _error.postValue(result.exceptionOrNull()?.message)
            _loadingCurrentStatus.postValue(when(result.isSuccess){
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            })
        }
    }
    fun loadMovieTrailer(movieId: Int, auth: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DetailsViewModel", "Attempting to load trailer for movie ID: $movieId")
            _loadingCurrentStatus.postValue(LoadingStatus.LOADING)
            val result = detailsRepository.getMovieTrailerKey(movieId, auth)
            if (result.isSuccess) {
                val trailerKey = result.getOrNull()
                Log.d("DetailsViewModel", "Successfully loaded trailer key: $trailerKey")
                _trailerKey.postValue(trailerKey)
                _loadingCurrentStatus.postValue(LoadingStatus.SUCCESS)
            } else {
                val errorMessage = result.exceptionOrNull()?.message
                Log.e("DetailsViewModel", "Failed to load trailer: $errorMessage")
                _error.postValue(result.exceptionOrNull()?.message)
                _loadingCurrentStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }




}