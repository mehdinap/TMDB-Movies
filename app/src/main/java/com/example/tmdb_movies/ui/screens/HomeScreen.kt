package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tmdb_movies.R
import com.example.tmdb_movies.data.AppType
import com.example.tmdb_movies.model.Movie
import com.example.tmdb_movies.ui.theme.TMDBMoviesTheme

@Composable
fun HomeScreen(
    movieCategories: List<MovieCategory>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val isError = movieCategories.any { it.uiState is MovieUiState.Error }
    val navigationItemContentList = listOf(
        NavigationItemContent(appType = AppType.Movie, text = "Movie"),
        NavigationItemContent(appType = AppType.TV, text = "TV"),
    )
    if (isError) {
        ErrorScreen(
            retryAction = retryAction,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Column {
            Row(Modifier.weight(100F)) {
                LazyColumn(
                    contentPadding = contentPadding,
                    modifier = modifier.fillMaxSize()
                ) {
                    items(items = movieCategories) { category ->
                        when (category.uiState) {
                            is MovieUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxWidth())
                            is MovieUiState.Success -> MovieCategoryRow(
                                title = category.title,
                                movies = category.uiState.movie,
                                modifier = Modifier.fillMaxWidth()
                            )

                            MovieUiState.Error -> TODO()
                        }
                    }
                }
            }
            Row(Modifier.weight(6.5F)) {
                BottomNavigationBar(
                    currentTab = AppType.Movie,
                    onTabPressed = { appType ->
                    },
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun MovieCategoryRow(
    title: String,
    movies: List<Movie>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(start = 8.dp)) {
        Text(text = title, fontSize = 22.sp, modifier = Modifier.padding(start = 10.dp))
        LazyRow(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .height(250.dp)
        ) {
            items(items = movies, key = { movie -> movie.id }) { movie ->
                MovieCard(
                    movie = movie,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .width(170.dp)
                )
            }
        }
    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Loading"
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "Failed to load", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("retry")
        }
    }
}

@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//        onClick = ,
    ) {
        AsyncImage(
            model = movie.fullPosterUrl,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = "NULL",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(text = movie.title)
    }
}


@Composable
private fun BottomNavigationBar(
    currentTab: AppType,
    onTabPressed: (AppType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.appType,
                onClick = { onTabPressed(navItem.appType) },
                icon = { Text(navItem.text) },
            )
        }
    }
}

data class NavigationItemContent(
    val appType: AppType, val text: String
)

//@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    TMDBMoviesTheme {
        LoadingScreen()
    }
}

//@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    TMDBMoviesTheme {
        ErrorScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TMDBMoviesTheme {
        val mockData = listOf(
            MovieCategory(
                title = "Now Playing",
                uiState = MovieUiState.Success(
                      List(10) { Movie(id = "$it", title = "Movie $it", poster = "") }
                )
            ),
            MovieCategory(
                title = "Popular",
                uiState = MovieUiState.Success(
                    List(10) { Movie(id = "${it + 10}", title = "Movie ${it + 10}", poster = "") }
                )
            ),
            MovieCategory(
                title = "Top Rated",
                uiState = MovieUiState.Success(
                    List(10) { Movie(id = "${it + 20}", title = "Movie ${it + 20}", poster = "") }
                )
            ),
            MovieCategory(
                title = "Upcoming",
                uiState = MovieUiState.Loading
            )
        )
        HomeScreen(
            movieCategories = mockData,
            retryAction = {},
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp)
        )
    }
}


