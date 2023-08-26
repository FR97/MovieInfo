package fr97.movieinfo.data.entity

import androidx.room.*
import fr97.movieinfo.data.api.Genre
import fr97.movieinfo.feature.moviedetails.MovieDetailsModel

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val  id: Int = 0,
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
    val status: String = ""
)

fun MovieEntity.toModel() : MovieDetailsModel{
    return MovieDetailsModel(
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
        status)
}