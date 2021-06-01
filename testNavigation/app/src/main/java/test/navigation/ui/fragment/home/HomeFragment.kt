package test.navigation.ui.fragment.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import test.navigation.R
import test.navigation.store.Account
import test.navigation.ui.activity.main.MainActivity


class HomeFragment : Fragment() {

    private lateinit var menu: Menu
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null


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

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
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
                        bottom_navigation_view.menu.findItem(R.id.navigation_register).isChecked =
                            true
                    }
                    HomeViewPagerAdapter.PRINT_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_print).isChecked = true
                    }
                    HomeViewPagerAdapter.SETUP_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_setup).isChecked = true
                    }
                    HomeViewPagerAdapter.DEVELOP_PAGE -> {
                        bottom_navigation_view.menu.findItem(R.id.navigation_develop).isChecked =
                            true
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
        menu.clear()
        menu.add(
            0, 1, 1, menuIconWithText(
                ContextCompat.getDrawable(requireActivity(), R.drawable.icon_signout)!!,
                "Log out"
            )
        )

        (activity as MainActivity).supportActionBar?.apply {
            title = "  " + Account.USER_NAME
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.icon_dev)
            setDisplayUseLogoEnabled(true)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> {
                // xử lý khi click vô log out
                Log.i("Logout", " executive ")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun menuIconWithText(r: Drawable, title: String): CharSequence {
        r.setBounds(0, 0, r.intrinsicWidth, r.intrinsicHeight)
        val sb = SpannableString("    $title")
        val imageSpan = ImageSpan(r, ImageSpan.ALIGN_BOTTOM)
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sb
    }
}