package com.example.tmdb_movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies", primaryKeys = ["id", "genreId"])
data class MovieEntity(
    val id: String,
    val title: String,
    val poster: String,
    val overview: String,
    val genreId: String,
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: String,
    val name: String
)