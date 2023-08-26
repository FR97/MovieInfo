package fr97.movieinfo.feature.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.work.impl.utils.LiveDataUtils
import fr97.movieinfo.core.di.Injector
import fr97.movieinfo.core.util.PagingSizes.INITIAL_LOAD_SIZE_HINT
import fr97.movieinfo.core.util.PagingSizes.PAGE_SIZE
import fr97.movieinfo.core.util.PagingSizes.PREFETCH_DISTANCE
import fr97.movieinfo.data.MovieRepository
import fr97.movieinfo.data.entity.MovieEntity

class MovieListViewModel(private val repository: MovieRepository, private val filter: String) : ViewModel() {

    private lateinit var movieList: LiveData<PagedList<MovieListItemModel>>

    init {
        val movieDataFactory = repository.getMovieDataSourceFactory(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
        movieList = LivePagedListBuilder<Int, MovieListItemModel>(movieDataFactory, config)
            .setFetchExecutor(Injector.appExecutors.networkIO())
            .build()
    }

    fun getPagedMovieList(): LiveData<PagedList<MovieListItemModel>> {
        return movieList
    }


}

class MovieListViewModelFactory(private val repository: MovieRepository, private val filter: String) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieListViewModel(repository, filter) as T
    }


}