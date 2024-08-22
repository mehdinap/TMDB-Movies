package com.example.tmdb_movies.data.paging

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.TMDBApiService
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(
    private val apiService: TMDBApiService,
    private val genreId: String
) : PagingSource<Int, Movie>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            // Fetch data from the API
            val movies = MovieAdapter.moviesOfResponse(apiService.getMovieByGenre(genreId, page))

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}