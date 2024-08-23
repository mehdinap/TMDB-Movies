import com.example.tmdb_movies.data.GenreEntity
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.data.MovieGenreCrossRef
import com.example.tmdb_movies.data.model.Genre
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.data.model.MovieApi

//fun MovieEntity.toMovie(genres: List<Int>): Movie {
//    return Movie(
//        id = this.id,
//        title = this.title,
//        poster = this.poster ?: "",
//        overview = this.overview ?: "",
//        genres = genres
//    )
//}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        poster = this.poster,
        overview = this.overview
    )
}

fun GenreEntity.toGenre(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}

fun Genre.toEntity(): GenreEntity {
    return GenreEntity(
        id = this.id,
        name = this.name
    )
}

fun MovieApi.toMovieGenreCrossRefs(): List<MovieGenreCrossRef> {
    return genres.map { genre ->
        MovieGenreCrossRef(
            movieId = this.id,
            genreId = genre
        )
    }
}

fun MovieApi.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        poster = this.poster,
        overview = this.overview
    )
}

fun MovieApi.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        poster = this.poster,
        overview = this.overview
    )
}