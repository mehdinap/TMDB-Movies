package com.example.tmdb_movies.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.data.AppDatabase
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.model.RemoteKeys
import com.example.tmdb_movies.network.TMDBApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import toEntity
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val genreId: String,
    private val moviesApiService: TMDBApiService,
    private val db: AppDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction =
        withContext(Dispatchers.IO) {
            val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
            val creationTime = db.getRemoteKeysDao().getCreationTime()
            return@withContext if (System.currentTimeMillis() - (creationTime
                    ?: 0) < cacheTimeout
            ) {
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val movies = MovieAdapter.moviesOfResponse(
                moviesApiService.getMovieByGenre(genreId, page = page)
            )
            val endOfPaginationReached = movies.isEmpty()
            withContext(Dispatchers.IO) {
                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        db.getRemoteKeysDao().clearRemoteKeys()
                    }

                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = movies.map {
                        RemoteKeys(
                            movieID = it.id,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = nextKey
                        )
                    }

                    db.getRemoteKeysDao().insertAll(keys)
                    movies.forEach {
                        db.getMovieDao().insertMovie(it.toEntity(genreId))
                    }
                }

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? =
        withContext(Dispatchers.IO) {
            return@withContext state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { movie ->
                    db.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
                }
        }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? =
        withContext(Dispatchers.IO) {
            return@withContext state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { movie ->
                    db.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
                }
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? =
        withContext(Dispatchers.IO) {
            return@withContext state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { movieId ->
                    db.getRemoteKeysDao().getRemoteKeyByMovieID(movieId)
                }
            }
        }
}