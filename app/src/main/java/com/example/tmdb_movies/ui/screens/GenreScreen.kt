package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.tmdb_movies.R
import com.example.tmdb_movies.model.Movie

@Composable
fun GenreScreen(
    genreViewModel: GenreViewModel,
    detailViewModel: DetailViewModel,
    genreId: String = "28", // Pass genreId to fetch movies
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moviePagingItems = genreViewModel.moviesPagingData.collectAsLazyPagingItems()

    Column(modifier = modifier.fillMaxSize()) {

        Text(text = moviePagingItems.itemCount.toString())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(moviePagingItems) { movie ->
//                MovieCard(
//                    movie = movie!!,
//                    detailViewModel = detailViewModel,
//                    cardClicked = {},
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .fillMaxHeight()
//                        .width(170.dp)
//                        .height(300.dp)
//                )
                var imagePainter: Painter? by remember { mutableStateOf(null) }
                Column(Modifier.fillMaxSize().width(150.dp).height(220.dp)) {
//                    Card(
//                        modifier = modifier
//                            .fillMaxSize()
//                            .weight(100F).width(150.dp).height(200.dp),
//                        shape = MaterialTheme.shapes.medium,
//                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//                        onClick = {
//                            detailViewModel.setCardDetail(movie!!, imagePainter)
//
//                        },
//                    ) {
                        AsyncImage(model = movie!!.fullPosterUrl,
                            error = painterResource(R.drawable.ic_broken_image),
                            placeholder = painterResource(R.drawable.loading_img),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().weight(90F),
                            onSuccess = { painter ->
                                imagePainter = painter.painter
                            })
//                    }
                    Text(
                        text = movie!!.title,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(10F)
                            .sizeIn(maxWidth = 150.dp)
                            .padding(start = 2.dp)
                    )
                }
            }
        }

//            item {
//                Button(
//                    onClick = { genreViewModel.handleMoreClicked(genreId) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                ) {
//                    Text(text = "Load More")
//                }
//            }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    // Display each movie item (This is a placeholder, adjust based on your UI needs)
    Text(
        text = movie.title,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
}
