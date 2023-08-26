package fr97.movieinfo.feature.videoslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider
import fr97.movieinfo.core.common.LiveModel
import fr97.movieinfo.core.error.Error
import fr97.movieinfo.data.MovieRepository

class VideosListViewModel(repository: MovieRepository, movieId: Int) : ViewModel() {

    private val videosLive: LiveData<LiveModel<List<VideoModel>>> = repository.getVideos(movieId)

    fun getVideos(): LiveData<LiveModel<List<VideoModel>>> {
        return videosLive
    }


}

class VideosListViewModelFactory(
    private val repository: MovieRepository,
    private val movieId: Int
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideosListViewModel(repository, movieId) as T
    }

}