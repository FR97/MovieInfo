package fr97.movieinfo.feature.moviedetails

import fr97.movieinfo.data.api.Credits
import fr97.movieinfo.data.api.Genre
import fr97.movieinfo.data.entity.MovieEntity

data class MovieDetailsModel(
    val id: Int = 0,
    val title: String = "",
    val releaseDate: String = "",
    val runtime: Int = 0,
    val backdropPath: String = "",
    val posterPath: String = "",
    val overview: String = "",
    val genre: String = "",
    val hasVideo: Boolean = false,
    val voteAverage: Double = 0.0,
    val homepage: String = "",
    val status: String = "",
    val cast: List<String> = emptyList(),
    val crew: List<String> = emptyList()
)


fun MovieDetailsModel.toEntity(): MovieEntity {
    return MovieEntity(
        id,
        title,
        releaseDate,
        runtime,
        backdropPath,
        posterPath,
        overview,
        genre,
        hasVideo,
        voteAverage,
        homepage,
        status
    )
}