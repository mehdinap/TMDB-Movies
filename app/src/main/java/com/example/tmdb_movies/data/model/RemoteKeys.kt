package com.example.tmdb_movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false) val movieID: String,
    val prevKey: Int?,
    val currentPage: Int?,
    val nextKey: Int?,
    val createdAt: Long = System.currentTimeMillis()
)