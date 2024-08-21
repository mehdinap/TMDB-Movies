import com.example.tmdb_movies.data.GenreEntity
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.model.Genre
import com.example.tmdb_movies.model.Movie

/*
* make a interface for mapper to DI, Repo local or remote.
* */
fun MovieEntity.toMovie(genreId: String): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        poster = this.poster,
//        backdrop = this.backdrop,
        overview = this.overview,
        genreId = genreId
    )
}


fun Movie.toEntity(genreId: String): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        poster = this.poster,
//        backdrop = this.backdrop,
        overview = this.overview,
        genreId = genreId
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
