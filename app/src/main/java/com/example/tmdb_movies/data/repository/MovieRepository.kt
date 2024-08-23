package com.example.tmdb_movies.data.repository

import android.os.Build
import android.provider.MediaStore.Audio.Genres
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PageKeyedDataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.cachedIn
import androidx.room.withTransaction
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.data.local.AppDatabase
import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.data.model.MovieApi
import com.example.tmdb_movies.data.paging.MoviesPagingSource
import com.example.tmdb_movies.data.service.TMDBApiService
import com.example.tmdb_movies.repository.MoviesRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import toEntity
import toGenre
import toMovie
import toMovieGenreCrossRefs

interface MovieRepository {
    suspend fun getMovieByGenres(genreId: String, page: Int): List<Movie>
    suspend fun getMovieGenres(): List<Genre>
    suspend fun getMoviesByGenrePaging(genreId: String): Flow<PagingData<Movie>>
    suspend fun saveMovie(movie: MovieApi)
    suspend fun saveGenre(genre: Genre)
    suspend fun getGenresFromLocal(): List<Genre>
    suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie>
}

class MovieRepositoryManagement(
    private val apiService: TMDBApiService, private val db: AppDatabase
) : MovieRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMovieByGenres(genreId: String, page: Int): List<Movie> =
        withContext(Dispatchers.IO) {
            val movies = MovieAdapter.moviesOfResponse(apiService.getMovieByGenre(genreId, page))
            movies.forEach { saveMovie(it) }
            return@withContext movies.map { it.toMovie() }
        }

    override suspend fun getMovieGenres(): List<Genre> = withContext(Dispatchers.IO) {
        val genres = MovieAdapter.genresOfResponse(apiService.getMovieGenres())
        genres.forEach { saveGenre(it) }
        genres
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMoviesByGenrePaging(genreId: String): Flow<PagingData<Movie>> =
        withContext(Dispatchers.IO) {
            Pager(config = PagingConfig(pageSize = 20),
                remoteMediator = MoviesRemoteMediator(
                    genreId = genreId, moviesApiService = apiService, db = db
                ),
                pagingSourceFactory = {
                    MoviesPagingSource(
                        dao = db.getMovieDao(), genreId = genreId
                    )
                }
            ).flow
        }

    override suspend fun saveMovie(movie: MovieApi) = withContext(Dispatchers.IO) {
        db.withTransaction {
            try {
                db.getMovieDao().insertMovie(movie.toEntity())
            } catch (e: Exception) {
                db.getMovieDao().insertMovie(
                    MovieEntity(
                        movie.id ?: "",
                        movie.title ?: "",
                        movie.poster ?: "",
                        movie.overview ?: "",
                    )
                )
            }
            db.getMovieDao().insertMovieGenreCrossRefs(movie.toMovieGenreCrossRefs())
        }
    }

    override suspend fun saveGenre(genre: Genre) = withContext(Dispatchers.IO) {
        db.getMovieDao().insertGenre(genre.toEntity())
    }

    override suspend fun getGenresFromLocal(): List<Genre> =
        withContext(Dispatchers.IO) {
            db.getMovieDao().getGenres().map { it.toGenre() }
        }

    override suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie> =
        withContext(Dispatchers.IO) {
            db.getMovieDao().getMoviesByGenre(genreId)
        }


}
