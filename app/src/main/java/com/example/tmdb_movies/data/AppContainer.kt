package com.example.tmdb_movies.data

import android.content.Context
import android.os.Build
import androidx.room.Room
import com.example.tmdb_movies.network.MTDBApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val movieRepository: MovieRepository
}

class DefaultAppContainer(
    private val database: AppDatabase // Receive the database instance
) : AppContainer {

    private val BASE_URL = "https://api.themoviedb.org/3/"
    private val ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxNmViNDZjNTJhZDExYWRlZjUwZTQ2ZDY0MWMxNjVmYiIsIm5iZiI6MTcyMzAyNjkwOC4wMjEwMzEsInN1YiI6IjY2YjM0OTVlNjY4ZGY3MjIxYmNkNmJiYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.DQZqHL7AyrIUi921AJVGWesqgSLgS80r1U3JWFbsI_k"

    private val okHttpClient =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $ACCESS_TOKEN")
                        .build()
                chain.proceed(request)
            }.build()

    private val retrofit: Retrofit =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build()

    private val retrofitService: MTDBApiService by lazy {
        retrofit.create(MTDBApiService::class.java)
    }

    override val movieRepository: MovieRepository by lazy {
        NetworkMovieRepository(
            retrofitService, database.movieDao()
        ) // Pass the DAO to the repository
    }
}
