package com.example.tmdb_movies.data

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.tmdb_movies.network.MTDBApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val marsPhotosRepository: MovieRepository
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original/"
    private val ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNmViNDZjNTJhZDExYWRlZjUwZTQ2ZDY0MWMxNjVmYiIsIm5iZiI6MTcyMzAyNjkwOC4wMjEwMzEsInN1YiI6IjY2YjM0OTVlNjY4ZGY3MjIxYmNkNmJiYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.DQZqHL7AyrIUi921AJVGWesqgSLgS80r1U3JWFbsI_k"

    private val retrofitPoster: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(POSTER_IMAGE_BASE_URL).build()

    private
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $ACCESS_TOKEN")
                .build()
            chain.proceed(request)
        }
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
//        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: MTDBApiService by lazy {
        retrofit.create(MTDBApiService::class.java)
    }

    override val marsPhotosRepository: MovieRepository by lazy {
        NetworkMovieRepository(retrofitService)
    }
}
