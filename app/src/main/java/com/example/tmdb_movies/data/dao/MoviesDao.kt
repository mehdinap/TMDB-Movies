package com.example.tmdb_movies.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdb_movies.data.GenreEntity
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.model.Movie

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genre: GenreEntity)

    @Query("SELECT * FROM movies WHERE genreId = :genreId")
    fun getMoviesByGenrePagingSource(genreId: String): PagingSource<Int, Movie>

    @Query("SELECT * FROM genres")
    fun getGenres(): List<GenreEntity>

    @Query("SELECT * FROM movies WHERE genreId = :genreId")
    fun getMoviesByGenre(genreId: String): List<MovieEntity>
}