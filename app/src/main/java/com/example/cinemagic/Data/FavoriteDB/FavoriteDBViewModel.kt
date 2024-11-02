package com.example.cinemagic.Data.FavoriteDB

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteDBViewModel ( application: Application) : AndroidViewModel(application){


    private val repository = FavoriteDBRepository(
        AppDatabase.getInstance(application).favoriteDBRepoDAO()
    )

    val getFavoriteEntries=

        repository.getFavoriteEntries().asLiveData()

    fun addFavoriteEntry(repo: FavoriteDBRepo) {

        CoroutineScope(Dispatchers.IO).launch {
            repository.insertFavoriteEntry(repo)
        }
    }

    fun updateFavoriteEntry(movieName:String, movieId:Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateFavoriteEntry(movieName,movieId)
        }
    }

    fun removeFavoriteEntry(repo: FavoriteDBRepo) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavoriteEntry(repo)
        }
    }

}