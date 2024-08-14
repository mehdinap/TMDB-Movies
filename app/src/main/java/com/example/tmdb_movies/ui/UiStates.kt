package com.example.tmdb_movies.ui

import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie

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
