package test.navigation.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import test.navigation.R
import test.navigation.ui.activity.main.MainActivity

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("HomeFragment", "onCreate")
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("HomeFragment", "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigationView()
        setupViewPager()
    }
    private fun setupBottomNavigationView() {

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_register -> {
                    home_view_pager.currentItem = HomeViewPagerAdapter.REGISTER_PAGE
                    true
                }
                R.id.navigation_print -> {
                    home_view_pager.currentItem = HomeViewPagerAdapter.PRINT_PAGE
                    true
                }
                R.id.navigation_setting -> {
                    home_view_pager.currentItem = HomeViewPagerAdapter.SETTING_PAGE
                    true
                }
                R.id.navigation_develop -> {
                    home_view_pager.currentItem = HomeViewPagerAdapter.DEVELOP_PAGE
                    true
                }
                else -> false
            }
        }
    }
    private fun setupViewPager() {
        (activity as MainActivity).supportActionBar?.title  = "HomeFragment"
        home_view_pager.adapter = HomeViewPagerAdapter(childFragmentManager)
        home_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    HomeViewPagerAdapter.REGISTER_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_register).isChecked = true
                        (activity as MainActivity).supportActionBar?.setTitle(R.string.title_register)
                    }
                    HomeViewPagerAdapter.PRINT_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_print).isChecked = true
                        (activity as MainActivity).supportActionBar?.setTitle(R.string.title_print)
                    }
                    HomeViewPagerAdapter.SETTING_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_print).isChecked = true
                        (activity as MainActivity).supportActionBar?.setTitle(R.string.title_setting)
                    }
                    HomeViewPagerAdapter.DEVELOP_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_print).isChecked = true
                        (activity as MainActivity).supportActionBar?.setTitle(R.string.title_dev)
                    }

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}