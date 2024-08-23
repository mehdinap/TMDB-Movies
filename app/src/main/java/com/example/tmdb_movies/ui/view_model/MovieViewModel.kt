package com.example.tmdb_movies.ui.view_model

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tmdb_movies.di.MovieApplication
import com.example.tmdb_movies.data.repository.MovieRepository
import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.ui.ui_state.GenresUiState
import com.example.tmdb_movies.ui.ui_state.MovieUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    var remoteGenres: List<Genre> by mutableStateOf(emptyList())
    var movieCategories: List<MovieCategory> by mutableStateOf(emptyList())
        private set

    init {
        getMovies()
    }

    /* for under API 31 crashed .  use alternative. */
    fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val check: GenresUiState = getGenresFromRepository {
                movieRepository.getMovieGenres()
            }
            when (check) {
                is GenresUiState.Success -> {
                    remoteGenres = check.movie
                    movieCategories = remoteGenres.take(6).map { genre ->
                        MovieCategory(genre = genre, uiState = getMoviesFromRepository {
                            movieRepository.getMovieByGenres(genre.id, 1)
                        })
                    }
                }

                is GenresUiState.Error -> {
                    remoteGenres = movieRepository.getGenresFromLocal()
                    movieCategories = remoteGenres.take(6).map { genre ->
                        MovieCategory(
                            genre = genre, uiState = MovieUiState.Success(
                                movieRepository.getMoviesByGenreFromLocal(genre.id)
                            )
                        )
                    }
                }

                is GenresUiState.Loading -> {
                    TODO()
                }
            }

        }
    }


    private suspend fun getMoviesFromRepository(fetch: suspend () -> List<Movie>): MovieUiState {
        return try {
            MovieUiState.Success(fetch())
        } catch (e: IOException) {
            Log.e("test", e.toString())
            MovieUiState.Error
        } catch (e: HttpException) {
            Log.e("test", e.toString())
            MovieUiState.Error
        } catch (e: Exception) {
            Log.e("test", e.toString())
            MovieUiState.Error
        }
    }

    private suspend fun getGenresFromRepository(fetch: suspend () -> List<Genre>): GenresUiState {
        return try {
            GenresUiState.Success(fetch())
        } catch (e: IOException) {
            Log.e("test", e.toString())
            GenresUiState.Error
        } catch (e: HttpException) {
            Log.e("test", e.toString())
            GenresUiState.Error
        } catch (e: Exception) {
            Log.e("test", e.toString())
            GenresUiState.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MovieApplication)
                val movieRepository = application.container.movieRepository
                MovieViewModel(movieRepository = movieRepository)
            }
        }
    }
}

data class MovieCategory(
    val genre: Genre, val uiState: MovieUiState
)