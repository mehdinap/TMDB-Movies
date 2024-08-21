package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.tmdb_movies.R

@Composable
fun GenreScreen(
    genreViewModel: GenreViewModel,
    detailViewModel: DetailViewModel,
    onBackClicked: () -> Unit,
    cardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moviePagingItems = genreViewModel.moviesPagingData.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        items(count = moviePagingItems.itemCount) { movie ->
            var imagePainter: Painter? by remember { mutableStateOf(null) }

            Card(modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 8.dp), onClick = {
                detailViewModel.setCardDetail(moviePagingItems[movie]!!, imagePainter)
                cardClicked()
            }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(model = moviePagingItems[movie]!!.fullPosterUrl,
                        error = painterResource(R.drawable.ic_broken_image),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.weight(3F),
                        onSuccess = { painter ->
                            imagePainter = painter.painter
                        })
                    Text(
                        text = moviePagingItems[movie]!!.title,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .align(Alignment.CenterVertically)
                            .weight(7F)
                    )
                }
            }
        }
    }
}

