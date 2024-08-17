package com.example.tmdb_movies.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)


@Serializable
data class Movie(
    val id: String,
    val title: String,
    @SerializedName(value = "poster_path") val poster: String,
    @SerializedName(value = "backdrop_path") val backdrop: String,
    val overview: String,
) {
    val fullPosterUrl: String
        get() = "https://image.tmdb.org/t/p/w342$poster"
    val fullBackdropUrl: String
        get() = "https://image.tmdb.org/t/p/w1280$backdrop"
}


@Serializable
data class GenresResponse(
    @SerializedName("genres") val genres: List<Genre>
)

@Serializable
data class Genre(
    val id: String,
    val name: String,
)


