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
import com.example.tmdb_movies.model.Movie
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieUiState {
    data class Success(val movie: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    init {
        getMovieUpcoming()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getMovieUpcoming() {
        viewModelScope.launch {
            movieUiState = MovieUiState.Loading
            movieUiState = try {
                MovieUiState.Success(movieRepository.getMovieUpcoming())
            } catch (e: IOException) {
                MovieUiState.Error
            } catch (e: HttpException) {
                MovieUiState.Error
            } catch (e: Exception) {
                MovieUiState.Error
            }
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
