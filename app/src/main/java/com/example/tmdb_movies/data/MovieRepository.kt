package com.example.tmdb_movies.data

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService

interface MovieRepository {
    suspend fun getMovieByGenres(genreId: String): List<Movie>
    suspend fun getMovieGenres(): List<Genre>

}
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class NetworkMovieRepository(
    private val mtdbApiService: MTDBApiService
) : MovieRepository {

    override suspend fun getMovieByGenres (genreId: String): List<Movie> {
        return MovieAdapter.moviesOfResponse(mtdbApiService.getMovieByGenre(genreId))
    }

    override suspend fun getMovieGenres(): List<Genre> {
        return MovieAdapter.genresOfResponse(mtdbApiService.getMovieGenres())
    }

}
