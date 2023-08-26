package fr97.movieinfo.feature.moviedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MovieDetailsPageAdapter(fragmentManager: FragmentManager, tabsCount: Int) :
    FragmentPagerAdapter(fragmentManager, tabsCount) {

    private val titles: MutableList<String> = mutableListOf()

    private val frag : MutableList<()->Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return frag[position]()
//        return when (position) {
//            0 -> MovieOverviewFragment.newInstance()
//            1 -> VideosListFragment.newInstance()
//            2 -> MemberListFragment.newInstance()
//            else -> {
//                throw IllegalStateException("Illegal position")
//            }
//        }
    }

    fun add(title: String, supplier: ()->Fragment){
        titles.add(title)
        frag.add(supplier)
    }

    override fun getCount(): Int = titles.size

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}