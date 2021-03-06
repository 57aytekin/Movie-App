package com.example.challengemovieapp.data.api

import com.example.challengemovieapp.BuildConfig
import com.example.challengemovieapp.data.entities.Movie
import com.example.challengemovieapp.data.entities.MovieResponse
import com.example.challengemovieapp.util.NetworkConnectionInterceptor
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = BuildConfig.API_KEY
const val BASE_URL = "http://www.omdbapi.com/"

interface MovieApiInterface {

    @GET("/")
    suspend fun getSearch(
        @Query("s") roverName : String,
        @Query("page") page : Int
    ): MovieResponse

    @GET("/")
    fun getMovieDetail(
        @Query("i") movieId : String
    ) : Single<Movie>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MovieApiInterface {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apikey", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApiInterface::class.java)
        }
    }
}