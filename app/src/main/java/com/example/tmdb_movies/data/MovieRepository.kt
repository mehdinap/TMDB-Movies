package com.example.tmdb_movies.data

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.TMDBApiService
import com.example.tmdb_movies.repository.MoviesRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import toEntity
import toGenre
import toMovie

interface MovieRepository {
    suspend fun getMovieByGenres(genreId: String, page: Int): List<Movie>
    suspend fun getMovieGenres(): List<Genre>
    fun getMoviesByGenrePaging(genreId: String): Flow<PagingData<Movie>>
    suspend fun saveMovie(movie: Movie, genreId: String)
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
            movies.forEach { saveMovie(it, genreId) }
            return@withContext movies
        }

    override suspend fun getMovieGenres(): List<Genre> = withContext(Dispatchers.IO) {
        val genres = MovieAdapter.genresOfResponse(apiService.getMovieGenres())
        genres.forEach { saveGenre(it) }
        return@withContext genres
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMoviesByGenrePaging(genreId: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(
                genreId = genreId,
                moviesApiService = apiService,
                db = db
            ),
            pagingSourceFactory = {
                db.getMovieDao().getMoviesByGenrePagingSource(genreId)
            }
        ).flow
    }

    override suspend fun saveMovie(movie: Movie, genreId: String) =
        withContext(Dispatchers.IO) {
            try {
                db.getMovieDao().insertMovie(movie.toEntity(genreId))
            } catch (e: Exception) {
                // Handle null values
                db.getMovieDao().insertMovie(
                    MovieEntity(
                        movie.id ?: "",
                        movie.title ?: "",
                        movie.poster ?: "",
                        movie.overview ?: "",
                        genreId
                    )
                )
            }
        }

    override suspend fun saveGenre(genre: Genre) = withContext(Dispatchers.IO) {
        db.getMovieDao().insertGenre(genre.toEntity())
    }

    override suspend fun getGenresFromLocal(): List<Genre> = withContext(Dispatchers.IO) {
        return@withContext db.getMovieDao().getGenres().map { it.toGenre() }
    }

    override suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie> =
        withContext(Dispatchers.IO) {
            return@withContext db.getMovieDao().getMoviesByGenre(genreId).map { it.toMovie(genreId) }
        }
}
