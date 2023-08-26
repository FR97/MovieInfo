package fr97.movieinfo.data.api

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{sorting}")
    fun getMovies(
        @Path("sorting") sorting: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = Client.TMDB_API_KEY,
        @Query("language") language: String = Client.LANG
    ): Call<MovieListResponse>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = Client.TMDB_API_KEY,
        @Query("language") language: String = Client.LANG,
        @Query("append_to_response") credits: String = "credits"
    ): Flowable<MovieDetailsResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = Client.TMDB_API_KEY,
        @Query("language") language: String = Client.LANG,
        @Query("page") page: Int
    ): Call<MovieReviewResponse>

    @GET("movie/{id}/videos")
    fun getVideos(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = Client.TMDB_API_KEY,
        @Query("language") language: String = Client.LANG
    ): Call<VideosListResponse>

}




data class MovieReviewResponse(val id: Int) {

}



























