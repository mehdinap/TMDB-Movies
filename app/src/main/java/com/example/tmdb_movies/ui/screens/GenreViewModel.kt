package com.example.tmdb_movies.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.ui.MovieUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GenreViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>?>(null)
    var movies: StateFlow<List<Movie>?> = _movies.asStateFlow()
        private set

    fun setMovies(movies: List<Movie>) {
        _movies.update { movies }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GenreViewModel()
            }
        }
    }
}