package fr97.movieinfo.core.di

import android.content.Context
import androidx.preference.PreferenceManager
import fr97.movieinfo.core.util.AppExecutors
import fr97.movieinfo.data.MovieRepository
import fr97.movieinfo.data.api.Client
import fr97.movieinfo.data.api.MovieApi
import fr97.movieinfo.data.db.MovieDb
import fr97.movieinfo.feature.moviedetails.MovieDetailsViewModelFactory
import fr97.movieinfo.feature.movielist.MovieListViewModelFactory
import fr97.movieinfo.feature.videoslist.VideosListViewModelFactory
import me.henrytao.rxsharedpreferences.RxSharedPreferences

object Injector {

    // Lazily initialized executors
    val appExecutors: AppExecutors by lazy { AppExecutors() }

    fun preferences(context: Context, preferences: String = ""): RxSharedPreferences {
        val prefs = if (preferences.isEmpty()) {
            PreferenceManager.getDefaultSharedPreferences(context)
        } else {
            context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
        }
        return RxSharedPreferences(prefs);
    }

    fun movieRepository(context: Context): MovieRepository {
        val database = MovieDb.getInstance(context.applicationContext)
        val movieApi = Client.retrofitClient.create(MovieApi::class.java)
        return MovieRepository.getInstance(database.movieDao(), movieApi, appExecutors)
    }

    fun movieListViewModelFactory(context: Context, filter: String): MovieListViewModelFactory {
        val repository = movieRepository(context.applicationContext)
        return MovieListViewModelFactory(repository, filter)
    }

    fun movieDetailsViewModelFactory(context: Context, movieId: Int): MovieDetailsViewModelFactory {
        val repository = movieRepository(context.applicationContext)
        return MovieDetailsViewModelFactory(repository)
    }

    fun videosListViewModelFactory(context: Context, movieId: Int): VideosListViewModelFactory {
        val repository = movieRepository(context.applicationContext)
        return VideosListViewModelFactory(repository, movieId)
    }


}