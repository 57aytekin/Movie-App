package com.example.challengemovieapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.challengemovieapp.data.api.MovieApiInterface
import com.example.challengemovieapp.data.entities.Search
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
class MoviePagingSource(
    private val movieApiInterface: MovieApiInterface,
    private val query : String
) : PagingSource<Int, Search>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = movieApiInterface.getSearch(query, position)
            var searchList = response.search
            if (searchList.isNullOrEmpty()) searchList = listOf()

                LoadResult.Page(
                    data = searchList,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (searchList.isNullOrEmpty()) null else position + 1
                )
        }catch (exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        TODO("Not yet implemented")
    }
}