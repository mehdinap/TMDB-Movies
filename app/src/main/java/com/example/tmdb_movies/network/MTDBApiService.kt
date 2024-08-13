package com.example.tmdb_movies.network

import com.example.tmdb_movies.model.GenresResponse
import com.example.tmdb_movies.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MTDBApiService {
    @GET("discover/movie")
    @Headers("accept: application/json")
    suspend fun getMovieByGenre(@Query("with_genres") genreId: String): Response<MovieResponse>

    @GET("genre/movie/list")
    @Headers("accept: application/json")
    suspend fun getMovieGenres(): Response<GenresResponse>


}