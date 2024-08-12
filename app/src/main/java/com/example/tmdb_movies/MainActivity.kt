package com.example.tmdb_movies

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tmdb_movies.ui.TMDBMoviesApp
import com.example.tmdb_movies.ui.theme.TMDBMoviesTheme

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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



@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview(showBackground = true)
@Composable
fun TMDBMovieAppPreview() {
    TMDBMoviesTheme {
        Surface {
            TMDBMoviesApp()
        }
    }
}
