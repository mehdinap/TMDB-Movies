package com.example.tmdb_movies.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tmdb_movies.R
import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.ui.theme.TMDBMoviesTheme
import com.example.tmdb_movies.ui.view_model.DetailViewModel
import com.example.tmdb_movies.ui.view_model.GenreViewModel
import com.example.tmdb_movies.ui.view_model.MovieCategory


@Composable
fun MovieCategoryRow(
    movieCategory: MovieCategory,
    movies: List<Movie>,
    detailViewModel: DetailViewModel,
    genreViewModel: GenreViewModel,
    cardClicked: () -> Unit,
    genreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(start = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = movieCategory.genre.name,
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 10.dp)
            )

            TextButton(content = { Text(text = "More >>", fontSize = 15.sp) }, onClick = {
                genreViewModel.loadMoviesForGenre(movieCategory.genre.id)
                genreClicked()
            })
        }

        LazyRow(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .height(250.dp)
        ) {
            items(items = movies, key = { movie -> movie.id }) { movie ->
                MovieCard(
                    movie = movie,
                    detailViewModel = detailViewModel,
                    cardClicked = cardClicked,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .width(150.dp)
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
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = "fail connection"
        )
        Text(text = "Failed to load", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("retry")
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    detailViewModel: DetailViewModel,
    cardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var imagePainter: Painter? by remember { mutableStateOf(null) }
    Column {
        Card(
            modifier = modifier
                .fillMaxSize()
                .weight(100F),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            onClick = {
                detailViewModel.setCardDetail(movie, imagePainter)
                cardClicked()
            },
        ) {
            AsyncImage(
                model = movie.fullPosterUrl,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onSuccess = { painter ->
                    imagePainter = painter.painter
                },
            )
        }
        Text(
            text = movie.title,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreList(
    genres: List<Genre>,
    genreViewModel: GenreViewModel,
    genreClicked: () -> Unit,
) {
    val gradientColors = listOf(Cyan, Color.Blue, Color.Red)

    LazyRow {
        items(items = genres) { genre ->
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(8.dp)
                    .height(40.dp)
            ) {
                TextButton(
                    content = {
                        Text(
                            text = genre.name,
                            modifier = Modifier
                        )
                    },
                    onClick = {
                        genreViewModel.loadMoviesForGenre(genre.id)
                        genreClicked()
                    },
                )

            }
        }
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
