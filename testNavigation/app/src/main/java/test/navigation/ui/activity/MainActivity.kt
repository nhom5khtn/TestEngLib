package test.navigation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import test.navigation.ui.fragment.home.HomeFragment
import test.navigation.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onResume() {
        super.onResume()
        Log.e("MainActivity", "onResume")

        // Get the text fragment instance
        val homeFragment = HomeFragment()

        // Get the support fragment manager instance
        val manager = supportFragmentManager

        // Begin the fragment transition using support fragment manager
        val transaction = manager.beginTransaction()

        // Replace the fragment on container
        transaction.replace(R.id.nav_host_fragment,homeFragment)
        transaction.addToBackStack(null)

        // Finishing the transition
        transaction.commit()


    }
}