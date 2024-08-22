package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.example.tmdb_movies.R
import com.example.tmdb_movies.data.AppType

enum class TMDBScreen() {
    Home, Detail, Genre, Search

}


data class NavigationItemContent(
    val appType: AppType, val text: String, val icon: Painter
)



@Composable
fun BottomNavigationBar(
    currentTab: AppType, onTabPressed: (AppType) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            appType = AppType.Movie,
            text = "Movie",
            icon = painterResource(
                id = R.drawable.movie_icon,
            ),
        ),
        NavigationItemContent(
            appType = AppType.TV,
            text = "TV Show",
            icon = painterResource(
                id = R.drawable.tv_show_icon,
            )
        ),
//        NavigationItemContent(
//            appType = AppType.Search,
//            text = "Sesrch",
//            icon = painterResource(
//                id = R.drawable.search_icon,
//            )
//        ),
    )
    val gradientColors = listOf(Cyan, Color.Blue, Color.Red)
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.appType,
                onClick = { onTabPressed(navItem.appType) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = navItem.icon,
                            contentDescription = null,
                            modifier = Modifier.weight(40F)
                        )
                        Text(
                            text = navItem.text, style = if (currentTab == navItem.appType) {
                                TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                )
                            } else {
                                MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            }, modifier = Modifier.weight(15F)
                        )
                    }
                },
            )
        }
    }

}