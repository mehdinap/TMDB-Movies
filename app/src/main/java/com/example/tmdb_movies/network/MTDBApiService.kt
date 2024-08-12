package com.example.tmdb_movies.network

import android.media.Image
import com.example.tmdb_movies.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface MTDBApiService {
    @GET("movie/now_playing")
    @Headers("accept: application/json")
    suspend fun getMovieNowPlaying(): Response<MovieResponse>

    @GET("movie/popular")
    @Headers("accept: application/json")
    suspend fun getMoviePopular(): Response<MovieResponse>

    @GET("movie/top_rated")
    @Headers("accept: application/json")
    suspend fun getMovieTopRated(): Response<MovieResponse>

    @GET("movie/upcoming")
    @Headers("accept: application/json")
    suspend fun getMovieUpcoming(): Response<MovieResponse>

    @GET("")
    @Headers("accept: application/json")
    suspend fun getMoviePoster(id: String): Image
}