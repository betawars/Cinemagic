package com.example.cinemagic.Data.FavoriteDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteDBRepo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract  fun favoriteDBRepoDAO():FavoriteDBRepoDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "favoritedbrepo"
            ).build()

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }

}