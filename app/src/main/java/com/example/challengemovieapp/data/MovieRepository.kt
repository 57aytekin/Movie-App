package com.example.challengemovieapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.challengemovieapp.data.api.MovieApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val apiInterface: MovieApiInterface) {

    fun getSearchResult(query : String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
        ),
            pagingSourceFactory = { MoviePagingSource(apiInterface, query)}
        ).liveData
}