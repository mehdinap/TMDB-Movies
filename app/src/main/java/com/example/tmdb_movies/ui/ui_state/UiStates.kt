package com.example.tmdb_movies.ui.ui_state

import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.data.model.Movie

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
