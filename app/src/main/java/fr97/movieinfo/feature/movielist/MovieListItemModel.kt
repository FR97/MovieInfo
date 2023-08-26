package fr97.movieinfo.feature.movielist

import com.google.gson.annotations.SerializedName

data class MovieListItemModel(
    val id: Int,
    val title: String,
    val thumbnailPath: String,
    val genres: String = ""
)