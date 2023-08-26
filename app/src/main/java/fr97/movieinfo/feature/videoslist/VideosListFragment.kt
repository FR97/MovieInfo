package fr97.movieinfo.feature.videoslist

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import fr97.movieinfo.R
import fr97.movieinfo.core.common.LiveModel
import fr97.movieinfo.core.di.Injector
import fr97.movieinfo.databinding.FragmentVideosListBinding
import kotlinx.android.synthetic.main.fragment_videos_list.view.*

class VideosListFragment : Fragment() {

    private var movieId: Int = -1

    private lateinit var viewModel: VideosListViewModel

    private lateinit var binding: FragmentVideosListBinding

    private lateinit var videosAdapter: VideosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_list, container, false)
        videosAdapter = VideosAdapter { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerViewVideos.layoutManager = layoutManager
        binding.recyclerViewVideos.adapter = videosAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieId = arguments?.getInt("movieId", -1) ?: -1
        viewModel = ViewModelProviders.of(
            this, Injector.videosListViewModelFactory(this.requireContext(), movieId)
        ).get(VideosListViewModel::class.java)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getVideos().observe(this, Observer {
            when (it) {
                is LiveModel.Loading -> showLoading(true)
                is LiveModel.Success -> onSuccess(it.data)
                is LiveModel.Failure -> onError(it.error.message)
            }
        })
    }

    private fun onSuccess(videos: List<VideoModel>) {
        binding.textOnError.visibility = View.GONE
        videosAdapter.replaceVideos(videos)
        showLoading(false)
    }

    private fun onError(message: String) {
        binding.textOnError.text = message
        binding.textOnError.visibility = View.VISIBLE
        showLoading(false)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.videoLoadingProgress.visibility = View.VISIBLE
        } else {
            binding.videoLoadingProgress.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(movieId: Int): VideosListFragment {
            val args = Bundle()
            args.putInt("movieId", movieId)
            val fragment = VideosListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
