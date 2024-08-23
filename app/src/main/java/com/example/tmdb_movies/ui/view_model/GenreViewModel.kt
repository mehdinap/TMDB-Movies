package com.example.tmdb_movies.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tmdb_movies.di.MovieApplication
import com.example.tmdb_movies.data.repository.MovieRepository
import com.example.tmdb_movies.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GenreViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _moviesPagingData = MutableStateFlow<PagingData<Movie>>(PagingData.empty())

    /* use another Ui state and expose that not  "moviesPagingData" */
    val moviesPagingData: StateFlow<PagingData<Movie>> get() = _moviesPagingData


    /* place Pager in Data or Domain Layer */
    fun loadMoviesForGenre(genreId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getMoviesByGenrePaging(genreId)
                .cachedIn(viewModelScope).collect{ pagingData ->
                    _moviesPagingData.update { pagingData }
                }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MovieApplication)
                GenreViewModel(movieRepository = application.container.movieRepository)
            }
        }
    }
}