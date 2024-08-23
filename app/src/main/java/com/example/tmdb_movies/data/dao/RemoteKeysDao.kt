package com.example.tmdb_movies.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdb_movies.data.model.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_key WHERE movieID = :movieId")
    fun getRemoteKeyByMovieID(movieId: String): RemoteKeys?

    @Query("DELETE FROM remote_key")
    fun clearRemoteKeys()

    @Query("SELECT createdAt FROM remote_key ORDER BY createdAt DESC LIMIT 1")
    fun getCreationTime(): Long?
}