package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdb_movies.data.model.AppType
import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.ui.navigation.BottomNavigationBar
import com.example.tmdb_movies.ui.ui_state.MovieUiState
import com.example.tmdb_movies.ui.view_model.DetailViewModel
import com.example.tmdb_movies.ui.view_model.GenreViewModel
import com.example.tmdb_movies.ui.view_model.MovieCategory


@Composable
fun HomeScreen(
    movieCategories: List<MovieCategory>,
    detailViewModel: DetailViewModel,
    genreViewModel: GenreViewModel,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    genreList: List<Genre>,
    cardClicked: () -> Unit,
    genreClicked: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    if (movieCategories.any { it.uiState is MovieUiState.Error }) {
        ErrorScreen(
            retryAction = retryAction, modifier = Modifier.fillMaxSize()
        )
    } else {
        Column {
            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .weight(100F)
                    .scrollable(
                        state = scrollState, orientation = Orientation.Vertical
                    )
            ) {
                LazyColumn(
//                    columns = GridCells.Fixed(1),
                    contentPadding = contentPadding, modifier = modifier
                        .fillMaxSize()
                        .weight(100F)
                ) {
                    item {
                        GenreList(genreList, genreViewModel, genreClicked)
                    }
                    items(items = movieCategories) { category ->
                        when (category.uiState) {
                            is MovieUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxWidth())
                            is MovieUiState.Success -> MovieCategoryRow(
                                movieCategory = category,
                                movies = category.uiState.movie,
                                detailViewModel = detailViewModel,
                                genreViewModel = genreViewModel,
                                cardClicked = cardClicked,
                                genreClicked = genreClicked,
                                modifier = Modifier.fillMaxWidth()
                            )

                            MovieUiState.Error -> TODO()
                        }
                    }
//                    if (movieCategories.isNotEmpty()) {
//                        item {
//                            GenreList(genreList, genreViewModel, genreClicked)
//                        }
//                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun previewNavigation() {
    BottomNavigationBar(
        AppType.Movie, { appType ->
        }, modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
    )
}