package fr97.movieinfo.feature.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import fr97.movieinfo.R
import fr97.movieinfo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
        const val GRID_SPAN_COUNT = 2
        const val TABS_COUNT = 4
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var homePageAdapter: HomePageAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setupTabLayout()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupTabLayout() {
        if (homePageAdapter == null)
            homePageAdapter = HomePageAdapter(childFragmentManager, TABS_COUNT)
        binding.viewPager.offscreenPageLimit = TABS_COUNT;
        binding.viewPager.adapter = homePageAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

}
