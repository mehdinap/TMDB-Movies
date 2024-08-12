package com.example.tmdb_movies.data

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService

interface MovieRepository {
    suspend fun getMovieNowPlaying(): List<Movie>
    suspend fun getMoviePopular(): List<Movie>
    suspend fun getMovieTopRated(): List<Movie>
    suspend fun getMovieUpcoming(): List<Movie>

}
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class NetworkMovieRepository(
    private val mtdbApiService: MTDBApiService
) : MovieRepository {
    override suspend fun getMovieNowPlaying(): List<Movie> {
        return MovieAdapter.moviesOfResponse(mtdbApiService.getMovieNowPlaying())
    }

    override suspend fun getMoviePopular(): List<Movie> {
        return MovieAdapter.moviesOfResponse(mtdbApiService.getMoviePopular())
    }

    override suspend fun getMovieTopRated(): List<Movie> {
        return MovieAdapter.moviesOfResponse(mtdbApiService.getMovieTopRated())
    }

    override suspend fun getMovieUpcoming(): List<Movie> {
        return MovieAdapter.moviesOfResponse(mtdbApiService.getMovieUpcoming())
    }

}
