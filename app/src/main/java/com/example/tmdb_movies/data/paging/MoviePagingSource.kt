package com.example.tmdb_movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb_movies.data.dao.MoviesDao
import com.example.tmdb_movies.data.model.Movie

class MoviesPagingSource(
    private val dao: MoviesDao,
    private val genreId: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = dao.getMoviesByGenrePagingSource(genreId)
                .load(params) as LoadResult.Page<Int, Movie>
            LoadResult.Page(
                data = page.data,
                prevKey = page.prevKey,
                nextKey = page.nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
