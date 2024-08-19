package com.example.tmdb_movies

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tmdb_movies.ui.TMDBMoviesApp
import com.example.tmdb_movies.ui.theme.TMDBMoviesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMDBMoviesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TMDBMoviesApp()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun TMDBMovieAppPreview() {
    TMDBMoviesTheme {
        Surface {
            TMDBMoviesApp()
        }
    }
}
