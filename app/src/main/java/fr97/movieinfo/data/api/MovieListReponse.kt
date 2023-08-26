package fr97.movieinfo.data.api

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("results")
    val movies: List<MovieListItemResponse>
) {
}

data class MovieListItemResponse(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val thumbnailPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("genre_ids")
    val genres: List<Int>
)
