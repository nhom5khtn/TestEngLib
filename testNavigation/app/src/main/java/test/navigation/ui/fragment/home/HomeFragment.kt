package test.navigation.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import test.navigation.R
import test.navigation.store.Account
import test.navigation.ui.activity.main.MainActivity

class HomeFragment : Fragment() {

    private lateinit var menu: Menu

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
        setHasOptionsMenu(true)
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
                R.id.navigation_setup -> {
                    home_view_pager.currentItem = HomeViewPagerAdapter.SETUP_PAGE
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
                    }
                    HomeViewPagerAdapter.PRINT_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_print).isChecked = true
                    }
                    HomeViewPagerAdapter.SETUP_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_setup).isChecked = true
                    }
                    HomeViewPagerAdapter.DEVELOP_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_develop).isChecked = true
                    }

                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("Home", "onCreateOption")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
        this.menu = menu
        menu[0].icon = ContextCompat.getDrawable(requireActivity(), R.drawable.icon_menu)
        (activity as MainActivity).supportActionBar?.apply {
            title = "  " + Account.USER_NAME
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.icon_dev)
            setDisplayUseLogoEnabled(true)
        }
    }
}