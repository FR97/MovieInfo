package fr97.movieinfo.feature.moviedetails

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider
import fr97.movieinfo.data.MovieRepository
import fr97.movieinfo.data.entity.MovieEntity
import fr97.movieinfo.feature.movielist.MovieListViewModel

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    // TODO: Implement the ViewModel


//    private val movieLiveData: LiveData<MovieDetailsModel>
//
//    init {
//
//        movieLiveData = repository.getMovie(movieId)
//
//    }

    private val isFavorite = MutableLiveData<Boolean>()

    fun getMovieDetails(movieId: Int): LiveData<MovieDetailsModel> {
        return repository.getMovie(movieId)
    }

    fun addToFavorite(movie: MovieDetailsModel) {
        repository.save(movie.toEntity())
//        isFavorite.postValue(true)
    }

    fun removeFromFavorite(movie: MovieDetailsModel) {
        repository.remove(movie.toEntity())
//        isFavorite.postValue(false)
    }

    @SuppressLint("CheckResult")
    fun isFavorite(movieId: Int): LiveData<Boolean> {
        repository.getFavoriteMovie(movieId)
            .subscribe({
                Log.d("WTFF", "Entity $it")
                isFavorite.postValue(true)
            }) {
                Log.d("WTFF", "Error $it")
                isFavorite.postValue(false)
            }
        return isFavorite;
    }

}

class MovieDetailsViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(repository) as T
    }


}