package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tmdb_movies.ui.view_model.DetailViewModel

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    modifier: Modifier.Companion,
) {
    val movie = detailViewModel.uiState.collectAsState().value
    // use the Flow not state. the ovbserv broke.
    val painter = detailViewModel.imagePainter

    /*
    * Ui Data
    * */

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .scrollable(
                state = scrollState, orientation = Orientation.Vertical
            )
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Card(
                modifier = modifier
                    .padding(start = 40.dp, top = 30.dp, end = 40.dp)
                    .width(250.dp)
                    .height(350.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                painter?.let {
                    Image(
                        contentScale = ContentScale.Crop,
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.End),
                    )
                }
            }
        }
        Row {
            Text(
                text = movie!!.title, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), fontSize = 30.sp
            )
        }
        Text(
            text = movie!!.overview, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), fontSize = 20.sp
        )
    }
}
