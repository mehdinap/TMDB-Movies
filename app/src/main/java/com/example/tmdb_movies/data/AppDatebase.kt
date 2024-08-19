package com.example.tmdb_movies.data

import com.example.tmdb_movies.data.dao.MovieDao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, GenreEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

