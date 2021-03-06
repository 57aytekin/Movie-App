package com.example.challengemovieapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.challengemovieapp.data.MovieRepository
import kotlinx.coroutines.CoroutineScope

class MovieViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel(){
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    val searchList = currentQuery.switchMap {queryString ->
        repository.getSearchResult(queryString).cachedIn(viewModelScope)
    }

    suspend fun movieDetail(movieId : String)  = repository.getMovieDetail(movieId)

    fun searchMovie(query : String){
        currentQuery.value = query
    }

    companion object{
        private const val DEFAULT_QUERY = "batman"
    }
}