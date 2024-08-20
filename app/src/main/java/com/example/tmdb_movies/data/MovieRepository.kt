package com.example.tmdb_movies.data

import android.util.Log
import androidx.paging.PagingSource
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.network.MTDBApiService
import com.example.tmdb_movies.data.dao.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import toEntity
import toGenre
import toMovie

interface MovieRepository {
    suspend fun getMovieByGenres(genreId: String, page: Int): List<Movie>
    suspend fun getMovieGenres(): List<Genre>
    fun getMoviePagingSource(genreId: String): PagingSource<Int, Movie>
    suspend fun saveMovies(movies: List<Movie>, genreId: String)
    suspend fun saveGenre(genre: Genre)
    suspend fun getGenresFromLocal(): List<Genre>
    suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie>
}

class NetworkMovieRepository(
    private val apiService: MTDBApiService, private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getMovieByGenres(genreId: String, page: Int): List<Movie> =
        withContext(Dispatchers.IO) {
            val movies = MovieAdapter.moviesOfResponse(apiService.getMovieByGenre(genreId, page))
            saveMovies(movies, genreId)
            return@withContext movies
        }

    override suspend fun getMovieGenres(): List<Genre> = withContext(Dispatchers.IO) {
        val genres = MovieAdapter.genresOfResponse(apiService.getMovieGenres())
        genres.forEach { saveGenre(it) }
        return@withContext genres
    }

    override fun getMoviePagingSource(genreId: String): PagingSource<Int, Movie> {
        return MoviePagingSource(genreId, apiService, movieDao)
    }

    override suspend fun saveMovies(movies: List<Movie>, genreId: String) =
        withContext(Dispatchers.IO) {
            movies.forEach {
                try {
                    movieDao.insertMovie(it.toEntity(genreId))
                } catch (e: Exception) {
                    // this null handling because some movie poster have problem/overview. we null handling for all.
                    movieDao.insertMovie(
                        MovieEntity(
                            it.id ?: "",
                            it.title ?: "",
                            it.poster ?: "",
                            it.overview ?: "",
                            genreId ?: ""
                        )
                    )
                }
            }
        }

    override suspend fun saveGenre(genre: Genre) = withContext(Dispatchers.IO) {
        movieDao.insertGenre(genre.toEntity())
    }

    override suspend fun getGenresFromLocal(): List<Genre> = withContext(Dispatchers.IO) {
        return@withContext movieDao.getGenres().map { it.toGenre() }
    }

    override suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie> =
        withContext(Dispatchers.IO) {
            return@withContext movieDao.getMoviesByGenre(genreId).map { it.toMovie() }
        }
}
