package com.example.tmdb_movies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tmdb_movies.data.GenreEntity
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.data.MovieGenreCrossRef
import com.example.tmdb_movies.data.dao.MoviesDao
import com.example.tmdb_movies.data.dao.RemoteKeysDao
import com.example.tmdb_movies.data.model.RemoteKeys

@Database(
    entities = [MovieEntity::class, GenreEntity::class, RemoteKeys::class, MovieGenreCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MoviesDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "movie_database"
                )
            }
                .fallbackToDestructiveMigration()
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }

}
