package com.example.cinemagic.Data.FavoriteDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface FavoriteDBRepoDAO {

    @Query("SELECT * FROM favoritedbrepo")
    fun getAllRepo(): Flow<List<FavoriteDBRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(repo: FavoriteDBRepo)

    @Query("UPDATE favoritedbrepo SET movie_name = :movieName WHERE movie_id = :movieId")
    suspend fun update(movieName:String,movieId:Int)

    @Delete
    suspend fun delete(repo: FavoriteDBRepo)

}