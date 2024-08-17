package com.example.tmdb_movies.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService
import com.example.tmdb_movies.adapters.MovieAdapter

class MoviePagingSource(
    private val genreId: String,
    private val apiService: MTDBApiService
) : PagingSource<Int, Movie>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
//        Log.i("GOLABI","page:$page")
        return try {
            val response = apiService.getMovieByGenre(genreId, page)
            val movies = MovieAdapter.moviesOfResponse(response)

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
