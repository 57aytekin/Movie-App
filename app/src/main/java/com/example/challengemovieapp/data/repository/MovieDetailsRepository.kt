package com.example.challengemovieapp.data.repository

import androidx.lifecycle.LiveData
import com.example.challengemovieapp.data.api.MovieApiInterface
import com.example.challengemovieapp.data.entities.Movie
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsRepository @Inject constructor(private val apiInterface: MovieApiInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchMovieDetails (compositeDisposable : CompositeDisposable, movieId : String) : LiveData<Movie> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiInterface, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getDetailsNetworkState() : LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}