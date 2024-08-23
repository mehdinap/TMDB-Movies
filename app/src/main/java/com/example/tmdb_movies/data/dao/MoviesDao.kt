package com.example.tmdb_movies.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdb_movies.data.GenreEntity
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.data.MovieGenreCrossRef
import com.example.tmdb_movies.data.model.Movie

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieGenreCrossRefs(crossRefs: List<MovieGenreCrossRef>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieEntities(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genre: GenreEntity)

//    @Query("SELECT * FROM movies WHERE id IN (SELECT movieId FROM movie_genre_cross_ref WHERE genreId = :genreId)")
//    fun getMoviesByGenrePagingSource(genreId: String): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id IN (SELECT movieId FROM movie_genre_cross_ref WHERE genreId = :genreId)")
    fun getMoviesByGenre(genreId: String): List<Movie>

    @Query("SELECT * FROM genres")
    fun getGenres(): List<GenreEntity>

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

}

