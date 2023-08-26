package fr97.movieinfo.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import fr97.movieinfo.core.common.LiveModel
import fr97.movieinfo.core.di.Injector
import fr97.movieinfo.core.error.Error
import fr97.movieinfo.core.util.AppExecutors
import fr97.movieinfo.data.api.*
import fr97.movieinfo.data.db.MovieDao
import fr97.movieinfo.data.entity.MovieEntity
import fr97.movieinfo.data.entity.toModel
import fr97.movieinfo.feature.moviedetails.MovieDetailsModel
import fr97.movieinfo.feature.movielist.MovieListItemModel
import fr97.movieinfo.feature.notification.NotificationWorker
import fr97.movieinfo.feature.videoslist.VideoModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration


class MovieRepository(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    executors: AppExecutors
) {

    fun getMovie(movieId: Int): LiveData<MovieDetailsModel> {
        return LiveDataReactiveStreams.fromPublisher(movieApi.getMovieDetails(movieId)
            .subscribeOn(Schedulers.io())
            .map {
                MovieDetailsModel(
                    it.id,
                    it.title,
                    it.releaseDate,
                    it.runtime,
                    it.backdropPath ?: "",
                    it.posterPath,
                    it.overview,
                    it.genres.joinToString { g -> g.name },
                    it.video,
                    it.voteAverage,
                    it.homepage ?: "",
                    it.status,
                    it.credits.cast.map { c -> "${c.name}  -  ${c.character}" },
                    it.credits.crew.map { c -> "${c.name}  -  ${c.job}" }
                )
            }
            .onErrorResumeNext { t: Throwable ->
                // When no internet load from local database (only favorites are saved)
                movieDao.findMovieByMovieId(movieId)
                    .map { it.toModel() }
                    .flatMapPublisher {
                        Flowable.fromArray(it)
                    }
            })

    }


    fun getMovieDataSourceFactory(sorting: String): DataSource.Factory<Int, MovieListItemModel> {
        return when (sorting) {
            "favorite" -> movieDao.findAllMoviesPaged().map {
                MovieListItemModel(
                    it.id,
                    it.title,
                    it.posterPath,
                    it.genre
                )
            }
            else -> RemoteMovieDataSourceFactory(sorting).map {
                MovieListItemModel(it.id, it.title, it.thumbnailPath ?: "")
            }
        }

    }


    fun getVideos(movieId: Int): LiveData<LiveModel<List<VideoModel>>> {
        val videosLive = MutableLiveData<LiveModel<List<VideoModel>>>()
        videosLive.postValue(LiveModel.Loading())
        movieApi.getVideos(movieId).enqueue(object : Callback<VideosListResponse> {
            override fun onResponse(
                call: Call<VideosListResponse>,
                response: Response<VideosListResponse>
            ) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        val videos = body?.results?.map { it.toModel() } ?: emptyList()
                        videosLive.postValue(LiveModel.Success(videos))
                    }
                    response.code() == Client.RESPONSE_CODE_AUTH_FAILED -> videosLive.postValue(
                        LiveModel.Failure(Error.NetworkError("Invalid Api Key"))
                    )
                    else -> videosLive.postValue(LiveModel.Failure(Error.NetworkError("Network Error: ${response.code()}")))
                }

            }

            override fun onFailure(call: Call<VideosListResponse>, t: Throwable) {
                videosLive.postValue(LiveModel.Failure(Error.NetworkError("Failed to load data from Internet")))
            }
        })

        return videosLive
    }


    fun save(movie: MovieEntity): Disposable {
        return Single.just(movie)
            .subscribeOn(Schedulers.io())
            .subscribe { m ->
                movieDao.saveMovie(m)
            }
    }

    @SuppressLint("CheckResult")
    fun remove(movie: MovieEntity) {
        Single.just(movie)
            .subscribeOn(Schedulers.io())
            .subscribe { m -> movieDao.deleteMovie(m) }
    }


    fun getFavoriteMovie(id: Int): Single<MovieDetailsModel> {
        return movieDao.findMovieByMovieId(id)
            .subscribeOn(Schedulers.io())
            .map { it.toModel() }
    }

    companion object {
        private var instance: MovieRepository? = null
        @Synchronized
        fun getInstance(
            movieDao: MovieDao,
            movieApi: MovieApi,
            executors: AppExecutors = Injector.appExecutors
        ): MovieRepository {
            if (instance == null)
                instance = MovieRepository(movieDao, movieApi, executors)

            return instance ?: throw IllegalStateException("MovieRepository can't be null")
        }
    }
}