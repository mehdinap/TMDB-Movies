package com.example.tmdb_movies

import android.app.Application
import android.os.Build
import androidx.room.Room
import com.example.tmdb_movies.data.AppContainer
import com.example.tmdb_movies.data.AppDatabase
import com.example.tmdb_movies.data.DefaultAppContainer

class MovieApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "movie_database"
        ).build()

        container = DefaultAppContainer(database)
    }
}
