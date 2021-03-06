package com.example.challengemovieapp.data.entities

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search")
    val search : List<Search>
)