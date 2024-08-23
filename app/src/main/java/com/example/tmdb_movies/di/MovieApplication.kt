package com.example.tmdb_movies.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension

class MovieApplication : Application() {
    lateinit var container: AppContainer
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}