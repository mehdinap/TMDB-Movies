package com.example.tmdb_movies.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdb_movies.data.GenreEntity
import com.example.tmdb_movies.data.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genre: GenreEntity)

    @Query("SELECT * FROM movies WHERE genreId = :genreId")
    fun getMoviesByGenre(genreId: String): List<MovieEntity>

    @Query("SELECT * FROM genres")
    fun getGenres(): List<GenreEntity>
}
