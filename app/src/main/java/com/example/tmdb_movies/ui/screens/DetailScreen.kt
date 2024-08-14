package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import java.lang.reflect.Modifier

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    onBackClicked: (Int) -> Unit,
) {
    val movie = detailViewModel.uiState.collectAsState().value
    val painter = detailViewModel.imagePainter

    if (movie != null && painter != null) {
        Column {
            Image(
                painter = painter,
                contentDescription = null,
//                modifier = Modifier.fillMaxSize()
            )
            Text(text = movie.title)
            Text(text = movie.overview)
        } ?: Text(text = "No movie selected.")
    }
}