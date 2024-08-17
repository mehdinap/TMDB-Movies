package com.example.tmdb_movies.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tmdb_movies.MovieApplication
import com.example.tmdb_movies.data.MovieRepository
import com.example.tmdb_movies.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GenreViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _moviesPagingData = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val moviesPagingData: StateFlow<PagingData<Movie>> get() = _moviesPagingData
//    val m: MutableList<PagingData<Movie>> = mutableListOf()
    private var hasLoadedInitialPage = false

    fun getMoviesPaging(genreId: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            movieRepository.getMoviePagingSource(genreId)
        }.flow.cachedIn(viewModelScope)
    }

    fun loadMoviesForGenre(genreId: String) {
        viewModelScope.launch {
            getMoviesPaging(genreId).collect { pagingData ->
                    _moviesPagingData.update { pagingData }
//                    m.addAll(listOf(pagingData))
//                    Log.i("loaddddddddddd",m.size.toString())
                    hasLoadedInitialPage = true
                }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MovieApplication)
                val movieRepository = application.container.movieRepository
                GenreViewModel(movieRepository = movieRepository)
            }
        }
    }
}
