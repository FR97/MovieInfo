package fr97.movieinfo.feature.overview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import fr97.movieinfo.databinding.FragmentMovieOverviewBinding
import fr97.movieinfo.R

class MovieOverviewFragment : Fragment() {

    companion object {
        fun newInstance(rating: Double, plot: String) : MovieOverviewFragment{
            val args = Bundle()
            args.putDouble("rating", rating)
            args.putString("plot", plot)
            val fragment = MovieOverviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: MovieOverviewViewModel

    private lateinit var binding: FragmentMovieOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rating  = arguments?.getDouble("rating", 0.0)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_overview, container, false)
        binding.movieDetailsRating.text = "$rating/10"
        binding.movieDetailsOverview.text = arguments?.getString("plot", "No plot description")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieOverviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
