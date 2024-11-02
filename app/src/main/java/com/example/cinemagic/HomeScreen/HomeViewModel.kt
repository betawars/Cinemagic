package com.example.cinemagic.HomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemagic.ENUMS.LoadingStatus
import com.example.cinemagic.HomeScreen.data.HomeSearchResults
import com.example.cinemagic.HomeScreen.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: HomeRepository
) : ViewModel() {
    private val _nowShowingMovies = MutableLiveData<HomeSearchResults?>()
    val nowShowingMovies: LiveData<HomeSearchResults?> = _nowShowingMovies

    private val _popularMovies = MutableLiveData<HomeSearchResults?>()
    val popularMovies: LiveData<HomeSearchResults?> = _popularMovies

    private val _topRatedMovies = MutableLiveData<HomeSearchResults?>()
    val topRatedMovies: LiveData<HomeSearchResults?> = _topRatedMovies

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

//    init {
//        fetchMovies()
//    }

    fun fetchMovies(apiKey:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingStatus.postValue(LoadingStatus.LOADING)
            try {
                moviesRepository.getNowShowingMovies(apiKey).also {
                    it.onSuccess { movies -> _nowShowingMovies.postValue(movies) }
                    it.onFailure { exception -> _error.postValue(exception.message) }
                }
                moviesRepository.getPopularMovies(apiKey).also {
                    it.onSuccess { movies -> _popularMovies.postValue(movies) }
                    it.onFailure { exception -> _error.postValue(exception.message) }
                }
                moviesRepository.getTopRatedMovies(apiKey).also {
                    it.onSuccess { movies -> _topRatedMovies.postValue(movies) }
                    it.onFailure { exception -> _error.postValue(exception.message) }
                }
                _loadingStatus.postValue(LoadingStatus.SUCCESS)
            } catch (e: Exception) {
                _loadingStatus.postValue(LoadingStatus.ERROR)
                _error.postValue(e.message)
            }
        }
    }
}
