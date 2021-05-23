package test.navigation.ui.fragment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import test.navigation.ui.fragment.Develop.DevelopFragment
import test.navigation.ui.fragment.Setting.SettingFragment
import test.navigation.ui.fragment.print.PrintFragment
import test.navigation.ui.fragment.register.RegisterFragment

class HomeViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val fragmentList = listOf(RegisterFragment(), PrintFragment(), SettingFragment(), DevelopFragment())
    companion object {
        const val REGISTER_PAGE     = 0
        const val PRINT_PAGE        = 1
        const val SETTING_PAGE      = 2
        const val DEVELOP_PAGE      = 3
        const val MAX_PAGES         = 4
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return MAX_PAGES
    }
}

