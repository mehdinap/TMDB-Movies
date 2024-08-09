package com.example.tmdb_movies.data

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService

interface MovieRepository {
    suspend fun getMovie(): List<Movie>
//    suspend fun getMoviePoster():
}

class NetworkMovieRepository(
    private val mtdbApiService: MTDBApiService
) : MovieRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMovie(): List<Movie> {
        val response = mtdbApiService.getMovies()
        return MovieAdapter.moviesOfResponse(response)
    }

}
