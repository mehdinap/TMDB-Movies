package com.example.tmdb_movies.ui.screens

import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tmdb_movies.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow<Movie?>(null)
    val uiState: StateFlow<Movie?> = _uiState.asStateFlow()
    var imagePainter: Painter? = null
        private set

    var backPage: String = TMDBScreen.Detail.name

    fun setCardDetail(movie: Movie, painter: Painter?) {
        _uiState.update { movie }
        imagePainter = painter
    }
    /*
    * make a image loader for in memory image
    * */


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DetailViewModel()
            }
        }
    }
}
