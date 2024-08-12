package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
    movieUiState: MovieUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(appType = AppType.Movie, text = "Movie"),
        NavigationItemContent(appType = AppType.TV, text = "TV"),
    )
    when (movieUiState) {
        is MovieUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is MovieUiState.Success -> MovieGridScreen(
            movieUiState.movie,
            contentPadding = contentPadding,
            navigationItemContentList = navigationItemContentList,
            modifier = modifier.fillMaxWidth(),
        )

        is MovieUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
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
fun MovieGridScreen(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    navigationItemContentList: List<NavigationItemContent>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val scrollState = rememberScrollState()
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(100F)) {
            LazyColumn(contentPadding = contentPadding,
                modifier = Modifier.scrollable(
                    state = scrollState, orientation = Orientation.Vertical
                )
            ) {
                item {
                    repeat(6) {
                        Text(
                            text = "Action",
                            fontSize = 22.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )
                        LazyRow(
                            Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .height(250.dp),
//                            contentPadding =
                        ) {
                            items(items = movies, key = { movie -> movie.id }) { movie ->
                                MovieCard(
                                    movie,
                                    modifier = modifier
                                        .padding(4.dp)
                                        .fillMaxHeight()
                                        .width(170.dp)
                                )
                            }
                        }
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
fun PhotosGridScreenPreview() {
    TMDBMoviesTheme {
        val navigationItemContentList = listOf(
            NavigationItemContent(appType = AppType.Movie, text = "Movie"),
            NavigationItemContent(appType = AppType.TV, text = "TV"),
        )
        val mockData = List(10) { Movie("$it", "", "") }
        MovieGridScreen(
            mockData, contentPadding = PaddingValues(0.dp),
            navigationItemContentList = navigationItemContentList,
            modifier = Modifier.fillMaxWidth(),
        )
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
