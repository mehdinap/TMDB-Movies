package com.example.tmdb_movies.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Movie(
    val id: String,
    val title: String,
    @SerializedName(value = "poster_path")
    val poster: String
//    val poster_path: String
) {
    val fullPosterUrl: String
        get() = "https://image.tmdb.org/t/p/w500$poster"
}

@Serializable
data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)