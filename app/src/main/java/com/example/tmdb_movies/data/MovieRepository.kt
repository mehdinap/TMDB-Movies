package com.example.tmdb_movies.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService

interface MovieRepository {
    suspend fun getMovieByGenres(genreId: String): List<Movie>
    suspend fun getMovieGenres(): List<Genre>
    fun getMoviePagingSource(genreId: String): PagingSource<Int, Movie>
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class NetworkMovieRepository(
    private val apiService: MTDBApiService
) : MovieRepository {

    override suspend fun getMovieByGenres(genreId: String): List<Movie> {
        return MovieAdapter.moviesOfResponse(apiService.getMovieByGenre(genreId, 1))
    }

    override suspend fun getMovieGenres(): List<Genre> {
        return MovieAdapter.genresOfResponse(apiService.getMovieGenres())
    }

    override fun getMoviePagingSource(genreId: String): PagingSource<Int, Movie> {
        return MoviePagingSource(genreId, apiService)
    }
}
