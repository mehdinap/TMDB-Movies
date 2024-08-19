package com.example.tmdb_movies.adapters


/**
 * title can also come from StringRes
 *
 * enum class Screen(@StringRes title: Int) { ... }
 */
enum class AppScreen(val title: String) {
    ScreenA(title = "Screen A"), // start screen
    ScreenB(title = "Screen B"),
    ScreenC(title = "Screen C"),
}