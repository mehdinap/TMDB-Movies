package com.example.tmdb_movies.adapters

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.data.model.GenresResponse
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.data.model.MovieApi
import com.example.tmdb_movies.data.model.MovieResponse
import retrofit2.Response

class MovieAdapter {
    companion object {
        @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
        fun moviesOfResponse(response: Response<MovieResponse>): List<MovieApi> {
            if (response.isSuccessful) {
                val movieResponse = response.body()
                return movieResponse?.results ?: emptyList()
            } else {
                throw Exception("Failed to load movies: ${response.errorBody()?.string()}")
            }
        }

        fun genresOfResponse(response: Response<GenresResponse>): List<Genre> {
            if (response.isSuccessful) {
                val genreResponse = response.body()
                return genreResponse?.genres ?: emptyList()
            } else {
                throw Exception("Failed to load movies: ${response.errorBody()?.string()}")
            }

        }
    }
}