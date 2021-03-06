package com.example.challengemovieapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.challengemovieapp.data.entities.Movie
import com.example.challengemovieapp.data.repository.MovieDetailsRepository
import com.example.challengemovieapp.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel @ViewModelInject constructor(
    private val repository: MovieDetailsRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun movieDetail(movieId : String) : LiveData<Movie>{
        return repository.fetchMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        repository.getDetailsNetworkState()
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}