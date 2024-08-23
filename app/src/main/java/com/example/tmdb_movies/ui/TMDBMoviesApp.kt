package com.example.tmdb_movies.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_movies.data.model.AppType
import com.example.tmdb_movies.ui.navigation.BottomNavigationBar
import com.example.tmdb_movies.ui.screens.DetailScreen
import com.example.tmdb_movies.ui.view_model.DetailViewModel
import com.example.tmdb_movies.ui.screens.GenreScreen
import com.example.tmdb_movies.ui.view_model.GenreViewModel
import com.example.tmdb_movies.ui.screens.HomeScreen
import com.example.tmdb_movies.ui.view_model.MovieViewModel
import com.example.tmdb_movies.ui.screens.SearchScreen
import com.example.tmdb_movies.ui.navigation.TMDBScreen


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
        TMDBTopBarApp(
            scrollBehavior = scrollBehavior,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.popBackStack() },
        )
    }, bottomBar = {
        BottomNavigationBar(
            currentTab = AppType.Movie, onTabPressed = { newTab ->
            }, modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
        )
    }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = TMDBScreen.Home.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = TMDBScreen.Home.name) {
                    Box {
                        Column {
                            HomeScreen(
                                movieCategories = movieViewModel.movieCategories,
                                detailViewModel = detailViewModel,
                                genreViewModel = genreViewModel,
                                retryAction = { movieViewModel.getMovies() },
                                genreList = movieViewModel.remoteGenres,
                                cardClicked = {
                                    navController.navigate(TMDBScreen.Detail.name)
                                },
                                genreClicked = {
                                    navController.navigate(TMDBScreen.Genre.name)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                composable(route = TMDBScreen.Detail.name) {
                    DetailScreen(
                        detailViewModel, modifier = Modifier
                    )
                }
                composable(route = TMDBScreen.Genre.name) {
                    GenreScreen(
                        genreViewModel, detailViewModel = detailViewModel, cardClicked = {
                            navController.navigate(TMDBScreen.Detail.name)
                        }, modifier = Modifier
                    )
                }
                composable(route = TMDBScreen.Search.name) {
                    SearchScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDBTopBarApp(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "TMDB")
            }
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), modifier = modifier, navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigation"
                    )
                }
            }
        }, scrollBehavior = scrollBehavior
    )
}

//@Preview
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PreviewApp() {
    TMDBMoviesApp()
}