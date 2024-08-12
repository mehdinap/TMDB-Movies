package com.example.tmdb_movies

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.data.AppContainer
import com.example.tmdb_movies.data.DefaultAppContainer

class MovieApplication : Application() {
    lateinit var container: AppContainer
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}