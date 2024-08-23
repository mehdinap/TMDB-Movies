package com.example.tmdb_movies.data.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerializedName("results") val results: List<MovieApi>
)


/*  search about  */
@Immutable
@Stable


@Serializable
data class Movie(
    val id: String,
    val title: String,
    @SerializedName(value = "poster_path") val poster: String,
    val overview: String,
) {     // make in Base image URl
    val fullPosterUrl: String
        get() = "https://image.tmdb.org/t/p/w342$poster"
}
@Serializable
data class MovieApi(
    val id: String,
    val title: String,
    @SerializedName(value = "poster_path") val poster: String,
    val overview: String,
    @SerializedName(value = "genre_ids") val genres: List<Int>,
) {     // make in Base image URl
    val fullPosterUrl: String
        get() = "https://image.tmdb.org/t/p/w342$poster"
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