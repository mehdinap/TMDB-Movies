package com.example.tmdb_movies.data.paging

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.data.dao.MoviesDao
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.data.model.MovieApi
import com.example.tmdb_movies.data.service.TMDBApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import toMovie
import java.io.IOException

class MoviesPagingSource(
    private val dao: MoviesDao,
    private val apiService: TMDBApiService,
    private val genreId: String
) : PagingSource<Int, Movie>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getMovieByGenre(genreId, page)
            val movies: List<MovieApi> = MovieAdapter.moviesOfResponse(response)
            val endOfPaginationReached = movies.isEmpty()

            withContext(Dispatchers.IO) {
                dao.insertMovieEntities(movies.map {
                    MovieEntity(
                        id = it.id,
                        title = it.title,
                        poster = it.poster,
                        overview = it.overview
                    )
                })
            }

            LoadResult.Page(
                data = movies.map { it.toMovie() },
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (endOfPaginationReached) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
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
