import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.tmdb_movies.adapters.MovieAdapter
import com.example.tmdb_movies.data.MovieEntity
import com.example.tmdb_movies.data.local.AppDatabase
import com.example.tmdb_movies.data.model.Movie
import com.example.tmdb_movies.data.model.MovieApi
import com.example.tmdb_movies.data.model.RemoteKeys
import com.example.tmdb_movies.data.service.TMDBApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val genreId: String,
    private val moviesApiService: TMDBApiService,
    private val db: AppDatabase
) : RemoteMediator<Int, Movie>() {

        override suspend fun initialize(): InitializeAction = withContext(Dispatchers.IO) {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val creationTime = db.getRemoteKeysDao().getCreationTime()
        return@withContext if (System.currentTimeMillis() - (creationTime ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Movie>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val response = moviesApiService.getMovieByGenre(genreId, page = page)
            val movies: List<MovieApi> = MovieAdapter.moviesOfResponse(response)
            val endOfPaginationReached = movies.isEmpty()

            withContext(Dispatchers.IO) {
                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        db.getRemoteKeysDao().clearRemoteKeys()
//                        db.getMovieDao().clearAllMovies()
                    }

                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = movies.map {
                        RemoteKeys(
                            movieID = it.id,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = nextKey
                        )
                    }
                    db.getRemoteKeysDao().insertAll(keys)

                    movies.forEach { movie ->
                        try {
                            db.getMovieDao().insertMovie(movie.toEntity())
                        } catch (e: Exception) {
                            db.getMovieDao().insertMovie(
                                MovieEntity(
                                    movie.id ?: "",
                                    movie.title ?: "",
                                    movie.poster ?: "",
                                    movie.overview ?: "",
                                )
                            )
                        }
                        db.getMovieDao().insertMovieGenreCrossRefs(movie.toMovieGenreCrossRefs())
                    }
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? =
        withContext(Dispatchers.IO) {
            state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
                db.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
            }
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? =
        withContext(Dispatchers.IO) {
            state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie ->
                db.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
            }
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? =
        withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { movieId ->
                    db.getRemoteKeysDao().getRemoteKeyByMovieID(movieId)
                }
            }
        }
}
