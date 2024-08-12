package com.example.tmdb_movies.adapters

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.model.MovieResponse
import retrofit2.Response

class MovieAdapter {
    companion object {
        @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
        fun moviesOfResponse(response: Response<MovieResponse>): List<Movie> {
            if (response.isSuccessful) {
                val movieResponse = response.body()
                return movieResponse?.results ?: emptyList()
            } else {
                throw Exception("Failed to load movies: ${response.errorBody()?.string()}")
            }

        }
    }
}