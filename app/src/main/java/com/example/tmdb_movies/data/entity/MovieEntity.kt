package com.example.tmdb_movies.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String, val title: String, val poster: String?, val overview: String?
)
@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: String, val name: String
)

@Entity(
    tableName = "movie_genre_cross_ref",
    primaryKeys = ["movieId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["id"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieGenreCrossRef(
    val movieId: String,
    val genreId: Int
)