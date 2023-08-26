package fr97.movieinfo.data.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDataSource(val sorting: String) : PageKeyedDataSource<Int, MovieListItemResponse>() {
    private val TAG = MovieDataSource::class.java.simpleName
    private val movieApi = Client.retrofitClient.create(MovieApi::class.java)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieListItemResponse>
    ) {
        movieApi.getMovies(sorting, 1).enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        val movies = if (body != null) body.movies else emptyList()
                        callback.onResult(movies, 1, 2)
                    }
                    response.code() == Client.RESPONSE_CODE_AUTH_FAILED ->
                        Log.e(TAG, "Invalid Api key. Response code: " + response.code())
                    else -> Log.e(TAG, "Response Code: " + response.code())
                }
            }
            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                Log.e(TAG, "Failed initializing a PageList: " + t.message)
            }
        })
    }
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieListItemResponse>) {
        val currentPage = params.key
        movieApi.getMovies(sorting, currentPage).enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        val movies = if (body != null) body.movies else emptyList()
                        callback.onResult(movies, currentPage + 1)
                    }
                    response.code() == Client.RESPONSE_CODE_AUTH_FAILED -> Log.e(TAG, "Invalid Api Key")
                    else -> Log.e(TAG, "Response Code: " + response.code())
                }
            }
            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                Log.e(TAG, "Failed appending page:" + t.message)
            }
        })
    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieListItemResponse>) {}
}


class RemoteMovieDataSourceFactory(private val sorting: String) : DataSource.Factory<Int, MovieListItemResponse>() {

    val postLiveData: MutableLiveData<MovieDataSource> = MutableLiveData()

    lateinit var movieDataSource: MovieDataSource
/*
    init {
        postLiveData = MutableLiveData()
    }*/

    override fun create(): DataSource<Int, MovieListItemResponse> {
        movieDataSource = MovieDataSource(sorting)

        // Keep reference to the data source with a MutableLiveData reference
        // postLiveData = MutableLiveData()
        postLiveData.postValue(movieDataSource)

        return movieDataSource
    }
}
