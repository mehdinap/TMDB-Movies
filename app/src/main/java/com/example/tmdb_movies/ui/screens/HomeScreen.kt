package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.tmdb_movies.data.AppType
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.ui.MovieUiState
val navigationItemContentList = listOf(
    NavigationItemContent(appType = AppType.Movie, text = "Movie"),
    NavigationItemContent(appType = AppType.TV, text = "TV"),
)
@Composable
fun HomeScreen(
    movieCategories: List<MovieCategory>,
    detailViewModel: DetailViewModel,
    genreViewModel: GenreViewModel ,
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
            Column(Modifier.weight(100F)) {
                LazyColumn(
                    contentPadding = contentPadding, modifier = modifier
                        .fillMaxSize()
                        .weight(100F)
                ) {
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
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp)
                        .weight(10F)
                ) {
                    items(items = genreList, itemContent = { genre ->
                        Box {
                            Text(text = genre.name, modifier = Modifier.padding(8.dp))
                        }
                    })
                }
            }
            Row(Modifier.weight(6.5F)) {
                BottomNavigationBar(
                    currentTab = AppType.Movie,
                    onTabPressed = { newTab ->
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
fun BottomNavigationBar(
    currentTab: AppType,
    onTabPressed: (AppType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    val gradientColors = listOf(Cyan, Color.Blue, Color.Red)


    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.appType,
                onClick = { onTabPressed(navItem.appType) },
                icon = {
                    Text(
                        text = navItem.text,
                        style = if (currentTab == navItem.appType) {
                            TextStyle(
                                brush = Brush.linearGradient(
                                    colors = gradientColors
                                )
                            )
                        } else {
                            MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        }
                    )
                },
            )
        }
    }

}

data class NavigationItemContent(
    val appType: AppType, val text: String
)

//@Preview
@Composable
fun previewNavigation() {
    val navigationItemContentList = listOf(
        NavigationItemContent(appType = AppType.Movie, text = "Movie"),
        NavigationItemContent(appType = AppType.TV, text = "TV"),
    )
    BottomNavigationBar(
        AppType.Movie,
        { appType ->
        },
        navigationItemContentList = navigationItemContentList,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    )
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    TMDBMoviesTheme {
//        val mockData = listOf(
//            MovieCategory(
//                title = "Now Playing",
//                uiState = MovieUiState.Success(List(10) {
//                    Movie(
//                        id = "$it",
//                        title = "Movie $it",
//                        poster = ""
//                    )
//                })
//            ),
//            MovieCategory(
//                title = "Popular",
//                uiState = MovieUiState.Success(List(10) {
//                    Movie(
//                        id = "${it + 10}",
//                        title = "Movie ${it + 10}",
//                        poster = ""
//                    )
//                })
//            ),
//            MovieCategory(title = "Top Rated", uiState = MovieUiState.Success(List(10) {
//                Movie(
//                    id = "${it + 20}",
//                    title = "Movie ${it + 20}",
//                    poster = ""
//                )
//            })),
//            MovieCategory(
//                title = "Upcoming", uiState = MovieUiState.Loading
//            )
//        )
//        HomeScreen(
//            movieCategories = mockData,
//            retryAction = {},
////            genreList = listOf(
////                Genre("1", "test 1"),
////                Genre("2", "test 2"),
////                Genre("3", "test 3"),
////                Genre("4", "test 4"),
////                Genre("5", "test 5"),
////                Genre("6", "test 6"),
////                Genre("7", "test 7"),
////                Genre("8", "test 8"),
////            ),
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(0.dp)
//        )
//    }
//}


