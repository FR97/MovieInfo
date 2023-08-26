package fr97.movieinfo.feature.home

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import fr97.movieinfo.feature.movielist.MovieListFragment
import java.lang.IllegalStateException

class HomePageAdapter(fragmentManager: FragmentManager, tabsCount: Int) :
    FragmentStatePagerAdapter(fragmentManager, tabsCount) {

    private val titles: List<String> = listOf("Popular", "Top Rated", "Upcoming", "Favorite")

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> MovieListFragment.newInstance("popular")
            1 -> MovieListFragment.newInstance("top_rated")
            2 -> MovieListFragment.newInstance("upcoming")
            3 -> MovieListFragment.newInstance("favorite")
            else -> {
                throw IllegalStateException("Illegal position")
            }
        }
    }

    override fun getCount(): Int = titles.size

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}