package test.navigation.ui.fragment.quiz.question

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_prepare_question.*
import test.navigation.R
import test.navigation.model.dict.ParseWordList
import test.navigation.store.Account


class PrepareQuestionFragment : Fragment() {

    private lateinit var questionViewModel: QuestionViewModel
    var isReady = false

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
        if(Account.userpool == "") {
            isReady = true
        } else {
            Log.e("isReady", "not : need to prepare data for QuestionFragment")
            progress_bar_prepare_question.visibility = View.VISIBLE
            prepareUserPool()
            isReady = true
        }
    }

    override fun onStart() {
        Log.e("onStart", "check get api")
        Log.e("onStart", isReady.toString())

        super.onStart()
        progress_bar_prepare_question.visibility = View.GONE
        btn_begin.setOnClickListener {
            if (isReady) {
                if(Account.wordList?.isNotEmpty() == true)
                    findNavController().navigate(R.id.action_prepareQuestionFragment_to_questionFragment)
                else {
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
            } else {
                val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.apply {
                    setTitle("Waiting for loading data!")
                    setPositiveButton("Continue!") { _: DialogInterface, _: Int ->
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
        questionViewModel.getWord(ParseWordList.from(Account.userpool)).observe(viewLifecycleOwner, {
            activity?.runOnUiThread {
                progress_bar_prepare_question.visibility = View.GONE
            }
        })
    }
}