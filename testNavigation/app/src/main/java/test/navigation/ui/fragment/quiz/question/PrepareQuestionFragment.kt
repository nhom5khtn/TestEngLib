package test.navigation.ui.fragment.quiz.question

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_prepare_question.*
import test.navigation.R
import test.navigation.model.dict.ParseWordList
import test.navigation.store.Account


class PrepareQuestionFragment : Fragment() {

    lateinit var questionViewModel: QuestionViewModel
    private lateinit var database: DatabaseReference
    var hasGetAPI = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_prepare_question, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUserPool()
    }

    override fun onStart() {
        Log.e("onStart", "check get api")
        Log.i("onStart", hasGetAPI.toString())

        super.onStart()
        progress_bar_prepare_question.visibility = View.GONE
        btn_begin.setOnClickListener {
            if (hasGetAPI) {
                if(Account.wordList?.isNotEmpty() == true)
                    findNavController().navigate(R.id.action_prepareQuestionFragment_to_questionFragment)
                else {
                    val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
                    builder?.apply {
                        setTitle("Waiting for loading data!")
                        setPositiveButton("Continue!") { _: DialogInterface, _: Int ->
                        }
                        show()
                    }
                }
            } else {
                val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.apply {
                    setTitle("Oppsss!")
                    setMessage("You don't have any in your pool.")
                    setPositiveButton("Continue!") { _: DialogInterface, _: Int ->
                        findNavController().navigate(R.id.action_prepareQuestionFragment_to_homeFragment)
                    }
                    show()
                }
            }
        }

        btn_out.setOnClickListener {
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.apply {
                setTitle("Don't want to play!")
                setMessage("Do you want to quit?")
                setPositiveButton("Give Up!") { _: DialogInterface, _: Int ->
                    activity?.finish()
                }
                setNegativeButton("Continue!") { _: DialogInterface, _: Int ->
                }
                show()
            }
        }

    }
    private fun prepareUserPool(){
        progress_bar_prepare_question.visibility = View.VISIBLE
        database = Firebase.database.reference
        database.child("users")
            .child(Account.USER_ID)
            .child("wordList")
            .get().addOnSuccessListener { dataSnapshot ->
                Log.i("firebase_welcome", "Got value ${dataSnapshot.value}")
                if(dataSnapshot.value != null) {
                    Account.userpool = dataSnapshot.value.toString()
                    hasGetAPI = true
                    questionViewModel.getWord(ParseWordList.from(Account.userpool))
                }
            }.addOnFailureListener{
                Log.e("firebase_welcome", "Error getting data", it)
            }
    }
}