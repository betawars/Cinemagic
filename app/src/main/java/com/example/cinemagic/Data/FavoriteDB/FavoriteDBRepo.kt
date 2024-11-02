package com.example.cinemagic.Data.FavoriteDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteDBRepo (
    @PrimaryKey
    @ColumnInfo("movie_id")
    val movieId:Int,

    @ColumnInfo("movie_name")
    val movieName:String?

)