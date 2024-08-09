package com.example.tmdb_movies

import android.app.Application
import com.example.tmdb_movies.data.AppContainer
import com.example.tmdb_movies.data.DefaultAppContainer

class MovieApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}