package com.example.tmdb_movies.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_movies.MovieApplication
import com.example.tmdb_movies.R
import com.example.tmdb_movies.ui.screens.DetailScreen
import com.example.tmdb_movies.ui.screens.DetailViewModel
import com.example.tmdb_movies.ui.screens.GenreScreen
import com.example.tmdb_movies.ui.screens.GenreViewModel
import com.example.tmdb_movies.ui.screens.HomeScreen
import com.example.tmdb_movies.ui.screens.MovieViewModel

enum class TMDBScreen() {
    ShowCase, DetailPage, GenrePage

}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDBMoviesApp(
    navController: NavHostController = rememberNavController()
) {
    val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)
    val detailViewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory)
    val genreViewModel: GenreViewModel = viewModel(factory = GenreViewModel.Factory)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TMDBTopBarrApp(
            scrollBehavior = scrollBehavior,
            navigateUp = { navController.navigateUp() },
            canNavigateBack = navController.previousBackStackEntry != null
        )
    }) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = TMDBScreen.ShowCase.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = TMDBScreen.ShowCase.name) {
                    Box {
                        Column {
                            HomeScreen(
                                movieCategories = movieViewModel.movieCategories,
                                detailViewModel = detailViewModel,
                                genreViewModel = genreViewModel,
                                retryAction = { movieViewModel.getMovies() },
                                genreList = movieViewModel.remoteGenres,
                                cardClicked = {
                                    navController.navigate(TMDBScreen.DetailPage.name)
                                },
                                genreClicked = {
                                    navController.navigate(TMDBScreen.GenrePage.name)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                composable(route = TMDBScreen.DetailPage.name) {
                    DetailScreen(
                        detailViewModel,
                        onBackClicked = { navController.navigate(TMDBScreen.ShowCase.name) },
                        modifier = Modifier
                    )
                }
                composable(route = TMDBScreen.GenrePage.name) {
                    GenreScreen(
                        genreViewModel,
                        detailViewModel = detailViewModel,
                        onBackClicked = {

                            navController.navigate(TMDBScreen.ShowCase.name)
                        },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDBTopBarrApp(
    scrollBehavior: TopAppBarScrollBehavior,
    navigateUp: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(scrollBehavior = scrollBehavior, title = {
        Row {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back button"
                )
            }
        }
            Text(
                text = stringResource(R.string.app_name_top_bar),
                style = MaterialTheme.typography.headlineMedium
            )
    }
    })
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//@Preview
@Composable
fun PreviewApp() {
    TMDBMoviesApp()
}