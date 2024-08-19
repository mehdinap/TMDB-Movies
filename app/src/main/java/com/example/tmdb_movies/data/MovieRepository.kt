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
    suspend fun getMovieByGenres(genreId: String): List<Movie>
    suspend fun getMovieGenres(): List<Genre>
    fun getMoviePagingSource(genreId: String): PagingSource<Int, Movie>
    suspend fun saveMovie(movie: Movie)
    suspend fun saveGenre(genre: Genre)
//    suspend fun getGenresFromLocal(): List<Genre>
//    suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie>
}

class NetworkMovieRepository(
    private val apiService: MTDBApiService, private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getMovieByGenres(genreId: String): List<Movie> =
        withContext(Dispatchers.IO) {
            val movies = MovieAdapter.moviesOfResponse(apiService.getMovieByGenre(genreId, 1))
            movies.forEach {
                movieDao.insertMovie(it.toEntity(genreId))
            }
            return@withContext movies
        }

    override suspend fun getMovieGenres(): List<Genre> = withContext(Dispatchers.IO) {
        val genres = MovieAdapter.genresOfResponse(apiService.getMovieGenres())
        genres.forEach { movieDao.insertGenre(it.toEntity()) }
        return@withContext genres
    }

    override fun getMoviePagingSource(genreId: String): PagingSource<Int, Movie> {
        return MoviePagingSource(genreId, apiService, movieDao)
    }

    override suspend fun saveMovie(movie: Movie) = withContext(Dispatchers.IO) {
//            movieDao.insertMovie(movie.toEntity())
    }

    override suspend fun saveGenre(genre: Genre) = withContext(Dispatchers.IO) {
        movieDao.insertGenre(genre.toEntity())
    }

//            override suspend fun getGenresFromLocal(): List<Genre> = withContext(Dispatchers.IO) {
//                return@withContext movieDao.getGenres().map { it.toGenre() }
//            }
//
//            override suspend fun getMoviesByGenreFromLocal(genreId: String): List<Movie> =
//                withContext(Dispatchers.IO) {
//                    return@withContext movieDao.getMoviesByGenre(genreId).map { it.toMovie() }
//                }
}
