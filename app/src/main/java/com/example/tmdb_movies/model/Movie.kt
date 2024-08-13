package com.example.tmdb_movies.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Movie(
    val id: String,
    val title: String,
    @SerializedName(value = "poster_path")
    val poster: String
) {
    val fullPosterUrl: String
        get() = "https://image.tmdb.org/t/p/w342$poster"
}
@Serializable
data class Genre(
    val id:String,
    val name:String,
)

@Serializable
data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)

@Serializable
data class GenresResponse(
    @SerializedName("genres") val genres: List<Genre>
)