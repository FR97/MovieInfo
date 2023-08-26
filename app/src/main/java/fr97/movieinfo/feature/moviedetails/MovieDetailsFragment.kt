package fr97.movieinfo.feature.moviedetails

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import fr97.movieinfo.R
import fr97.movieinfo.core.common.App
import fr97.movieinfo.core.di.Injector
import fr97.movieinfo.data.api.ApiConstants
import fr97.movieinfo.databinding.FragmentMovieDetailsBinding
import fr97.movieinfo.feature.memberlist.MemberListFragment
import fr97.movieinfo.feature.notification.NotificationWorker
import fr97.movieinfo.feature.overview.MovieOverviewFragment
import fr97.movieinfo.feature.videoslist.VideosListFragment
import io.reactivex.disposables.CompositeDisposable
import java.lang.IllegalStateException
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MovieDetailsFragment : Fragment() {

    private lateinit var viewModel: MovieDetailsViewModel

    private lateinit var binding: FragmentMovieDetailsBinding

    private val args: MovieDetailsFragmentArgs by navArgs()

    private var movieDetailsModel: MovieDetailsModel? = null

    private var detailsPageAdapter: MovieDetailsPageAdapter? = null

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        setupFab()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders
            .of(this, Injector.movieDetailsViewModelFactory(this.requireContext(), args.movieId))
            .get(MovieDetailsViewModel::class.java)
        setupMovieDetailsObserver()
        setupFabObserver()
    }


    private fun setupTabs(movie: MovieDetailsModel) {
        val castList = ArrayList<String>()
        castList.addAll(movie.cast)

        val crewList = ArrayList<String>()
        crewList.addAll(movie.crew)
        if (detailsPageAdapter == null) {
            val adapter = MovieDetailsPageAdapter(childFragmentManager, 4)
            adapter.add("Overview") { MovieOverviewFragment.newInstance(movie.voteAverage, movie.overview) }
            adapter.add("Videos") { VideosListFragment.newInstance(movie.id) }
            adapter.add("Cast") { MemberListFragment.newInstance(castList, "cast") }
            adapter.add("Crew") { MemberListFragment.newInstance(crewList, "crew") }
            detailsPageAdapter = adapter
        }
        binding.viewPager.adapter = detailsPageAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            if (movieDetailsModel != null) {
                val isFav = viewModel.isFavorite(args.movieId).value ?: false
                if (!isFav) {
                    viewModel.addToFavorite(movieDetailsModel ?: throw IllegalStateException("Null value"))
                    scheduleNotification()
                } else {
                    viewModel.removeFromFavorite(movieDetailsModel ?: throw IllegalStateException("Null value"))
                    removeNotification()
                }
                setupFabObserver()
            } else {
                Log.d("ERROR", "Movie Details shouldn't be null")
            }
        }
    }

    private fun scheduleNotification() {
        val date = LocalDate.parse(movieDetailsModel?.releaseDate ?: "")
        val numberOfDaysBefore = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("daysBefore", "7")
        val daysFromNow = date.minusDays(numberOfDaysBefore?.toLong() ?: 7)
        val currentDate = LocalDate.now()
        val duration = Period.between(currentDate, daysFromNow).days.toLong()
        if(duration > 0){ // TODO Remove this if this if you want to test notification, it will send instantly for any movie

            val data = Data.Builder()
                .putInt("movieId", movieDetailsModel?.id ?: 0)
                .putString("movieTitle", movieDetailsModel?.title ?: "")
                .putString("releaseDate", movieDetailsModel?.releaseDate ?: "")
                .build()


            val updateWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .setInitialDelay(duration, TimeUnit.DAYS)
                .build()

            Log.d("Notifications", "Scheduled Notification for $daysFromNow, total days: $duration")
            WorkManager.getInstance(requireContext())
                .enqueueUniqueWork(App.UPDATE_WORK_NAME + movieDetailsModel?.id, ExistingWorkPolicy.KEEP, updateWork)
        }
    }

    private fun removeNotification() {
        Log.d("Notifications", "Removed notification " + App.UPDATE_WORK_NAME + movieDetailsModel?.id)
        WorkManager.getInstance(requireContext())
            .cancelUniqueWork(App.UPDATE_WORK_NAME + movieDetailsModel?.id)
    }

    private fun setupFabObserver() {
        viewModel.isFavorite(args.movieId).observe(this, Observer { isFav ->
            if (isFav) {
                binding.fab.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_remove_white_24dp))
            } else {
                binding.fab.setImageIcon(
                    Icon.createWithResource(
                        requireContext(), R.drawable.ic_favorite_border_white_24dp
                    )
                )
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupMovieDetailsObserver() {
        binding.loadingProgress.visibility = View.VISIBLE
        viewModel.getMovieDetails(args.movieId).observe(this, Observer {
            movieDetailsModel = it
            binding.movieDetailsTitle.text = it.title
            binding.movieDetailsGenre.text = "Genres: ${it.genre}"
            binding.movieDetailsRuntime.text = "Duration: ${it.runtime} min"
            binding.movieDetailsReleaseYear.text = "Release: " + LocalDate.parse(it.releaseDate)
                .format(DateTimeFormatter.ofPattern("dd MMM uuuu"))
            binding.loadingProgress.setProgress(50, true)

            val backdrop = ApiConstants.IMAGE_URL_BASE + ApiConstants.IMAGE_SIZE_REGULAR + it.backdropPath
            Glide.with(requireContext())
                .load(backdrop)
                .error(R.drawable.image_not_found)
                .into(binding.backdropImage)
            val poster = ApiConstants.IMAGE_URL_BASE + ApiConstants.IMAGE_SIZE_SMALL + it.posterPath

            Glide.with(requireContext())
                .load(poster)
                .error(R.drawable.image_not_found)
                .into(binding.posterImage)
            setupTabs(it)
            binding.loadingProgress.visibility = View.GONE
        })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}
