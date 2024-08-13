package com.example.tmdb_movies.ui.screens

import android.net.http.HttpException
import android.os.Build
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
import com.example.tmdb_movies.MovieApplication
import com.example.tmdb_movies.data.MovieRepository
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieUiState {
    data class Success(val movie: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

sealed interface GenresUiState {
    data class Success(val movie: List<Genre>) : GenresUiState
    object Error : GenresUiState
    object Loading : GenresUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    var remoteGenres: List<Genre> by mutableStateOf(emptyList())
    var movieCategories: List<MovieCategory> by mutableStateOf(emptyList())
        private set

    init {
        getMovies()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getMovies() {
        viewModelScope.launch {
            val check: GenresUiState = getGenresFromRepository { movieRepository.getMovieGenres() }
            when (check) {
                is GenresUiState.Success -> {
                    remoteGenres = check.movie
                    movieCategories = remoteGenres.take(6).map { genre ->
                        MovieCategory(title = genre.name, uiState = getMoviesFromRepository {
                            movieRepository.getMovieByGenres(
                                genre.id
                            )
                        })
                    }
                }

                is GenresUiState.Error -> {
                    movieCategories =
                        listOf(MovieCategory(title = "Fail", uiState = MovieUiState.Error))
                }

                is GenresUiState.Loading -> TODO()
            }
        }
    }

    private suspend fun getMoviesFromRepository(fetch: suspend () -> List<Movie>): MovieUiState {
        return try {
            MovieUiState.Success(fetch())
        } catch (e: IOException) {
            MovieUiState.Error
        } catch (e: HttpException) {
            MovieUiState.Error
        } catch (e: Exception) {
            MovieUiState.Error
        }
    }

    private suspend fun getGenresFromRepository(fetch: suspend () -> List<Genre>): GenresUiState {
        return try {
            GenresUiState.Success(fetch())
        } catch (e: IOException) {
            GenresUiState.Error
        } catch (e: HttpException) {
            GenresUiState.Error
        } catch (e: Exception) {
            GenresUiState.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MovieApplication)
                val movieRepository = application.container.marsPhotosRepository
                MovieViewModel(movieRepository = movieRepository)
            }
        }
    }
}

data class MovieCategory(
    val title: String, val uiState: MovieUiState
)
