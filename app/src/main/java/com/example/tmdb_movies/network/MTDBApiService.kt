package com.example.tmdb_movies.network

import android.media.Image
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.model.MovieResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface MTDBApiService {
    // https://api.themoviedb.org/
    @GET("discover/movie")
    @Headers("accept: application/json")
    suspend fun getMovies(): Response<MovieResponse>

}