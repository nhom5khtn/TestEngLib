package test.navigation.ui.fragment.quiz.question

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_prepare_question.*
import test.navigation.R
import test.navigation.store.Account


class PrepareQuestionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prepare_question, container, false)
    }

    override fun onStart() {

        super.onStart()
        progress_bar_prepare_question.visibility = View.GONE
        btn_begin.setOnClickListener {
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
        }

        btn_out.setOnClickListener {
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.apply {
                setTitle("You don't want to play?")
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
}