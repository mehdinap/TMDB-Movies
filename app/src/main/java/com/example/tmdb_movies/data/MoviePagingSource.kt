package com.example.tmdb_movies.data

import android.util.Log
import com.example.tmdb_movies.data.dao.MovieDao
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService
import com.example.tmdb_movies.adapters.MovieAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import toEntity
import toMovie


class MoviePagingSource(
    private val genreId: String,
    private val apiService: MTDBApiService,
    private val movieDao: MovieDao
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val movies = MovieAdapter.moviesOfResponse(apiService.getMovieByGenre(genreId, page))
            movies.forEach {
                try {
                    Log.i("INSERT BY PAGING",it.toEntity(genreId).toString())
                    movieDao.insertMovie(it.toEntity(genreId))
                } catch (e: Exception) {
                    // this null handling because some movie poster have problem/overview. we null handling for all.
                    Log.e("INSERT BY PAGING",e.toString())
                //                    movieDao.insertMovie(MovieEntity(it.id?:"", it.title?:"", it.poster?:"", it.overview?:"", genreId?:""))
                }
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
//            val cachedMovies = movieDao.getMoviesByGenre(genreId)
//            if (cachedMovies.isNotEmpty()) {
//                LoadResult.Page(
//                    data = cachedMovies.map { it.toMovie() },
//                    prevKey = if (page == 1) null else page - 1,
//                    nextKey = if (cachedMovies.isEmpty()) null else page + 1
//                )
//            } else {
//                LoadResult.Error(e)
//            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(1)
        }
    }
}



