package com.example.tmdb_movies.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tmdb_movies.R
import com.example.tmdb_movies.ui.screens.HomeScreen
import com.example.tmdb_movies.ui.screens.MovieViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDBMoviesApp() {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TMDBTopBarrApp(scrollBehavior = scrollBehavior) }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)
            Box {
                Column {
                    HomeScreen(
                        movieUiState = movieViewModel.movieUiState,
                        retryAction = movieViewModel::getMovie,
                        contentPadding = it,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDBTopBarrApp(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior, title = {
            Text(
                text = stringResource(R.string.top_bar_app_name),
                style = MaterialTheme.typography.headlineMedium
            )
        }, modifier = modifier
    )
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//@Preview
@Composable
fun PreviewApp() {
    TMDBMoviesApp()
}