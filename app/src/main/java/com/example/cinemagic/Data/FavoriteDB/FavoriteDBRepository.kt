package com.example.cinemagic.Data.FavoriteDB

import javax.inject.Inject

class FavoriteDBRepository @Inject constructor(
    private val dao: FavoriteDBRepoDAO)
{

    fun getFavoriteEntries() =
        dao.getAllRepo()
    suspend fun insertFavoriteEntry(repo: FavoriteDBRepo) =
        dao.insert(repo)

    suspend fun updateFavoriteEntry(movieName:String,movieId:Int) =
        dao.update(movieName,movieId)
    suspend fun deleteFavoriteEntry(repo: FavoriteDBRepo) =
        dao.delete(repo)
}