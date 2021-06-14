package test.navigation.ui.fragment.develop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import test.navigation.R
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DevelopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DevelopFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_develop, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.e("loadResult Before", "Got numQuest ${Account.RESULT[0]["numQuest"]?.toInt()}")
        Log.e("loadResult Before", "Got numCorrect ${Account.RESULT[0]["numCorrect"]?.toInt()}")
        Log.e("loadResult Before", "Got countTest ${Account.RESULT.size}")
    }
}