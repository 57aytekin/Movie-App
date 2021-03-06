package com.example.challengemovieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.challengemovieapp.data.api.MovieApiInterface
import com.example.challengemovieapp.data.entities.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(
    private val apiService : MovieApiInterface, private val compositeDisposable: CompositeDisposable
) {
    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse =  MutableLiveData<Movie>()
    val downloadedMovieResponse: LiveData<Movie>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: String){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetail(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    )
            )
        } catch (e: Exception){
            Log.e("MovieDetailsDataSource",e.message!!)
        }
    }

}