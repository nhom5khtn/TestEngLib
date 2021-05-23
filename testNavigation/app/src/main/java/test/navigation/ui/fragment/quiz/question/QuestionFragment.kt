package test.navigation.ui.fragment.quiz.question

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_question.*
import test.navigation.R
import test.navigation.model.question.Question
import test.navigation.store.Account
import test.navigation.store.Userpool

class QuestionFragment : Fragment() {

    private var mCurrentPosition: Int = 1 // Default and the first question position
    private var mQuestionsList: ArrayList<Question>? = null
    private var mCorrectAnswers: Int = 0
    private var mSelectedOptionPosition: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mQuestionsList = Account.getQuestions()
        setQuestion()

        tv_option_one.setOnClickListener {
            selectedOptionView(tv_option_one, 1)
        }
        tv_option_two.setOnClickListener {
            selectedOptionView(tv_option_two, 2)
        }
        tv_option_three.setOnClickListener {
            selectedOptionView(tv_option_three, 3)
        }
        tv_option_four.setOnClickListener {
            selectedOptionView(tv_option_four, 4)
        }

        btn_submit.setOnClickListener{
            if (mSelectedOptionPosition == 0) {
                mCurrentPosition++
                when {
                    mCurrentPosition <= mQuestionsList!!.size -> {
                        setQuestion()
                    }
                    else -> {
                        Account.CORRECT_ANSWERS = mCorrectAnswers
                        Account.TOTAL_QUESTIONS = mQuestionsList!!.size
                        findNavController().navigate(R.id.action_questionFragment_to_resultFragment)
                    }
                }
            } else {
                val question = mQuestionsList?.get(mCurrentPosition - 1)

                // This is to check if the answer is wrong
                if (question!!.correctAnswer != mSelectedOptionPosition) {
                    answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                } else {
                    mCorrectAnswers++
                }

                // This is for correct answer
                answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                if (mCurrentPosition == mQuestionsList!!.size) {
                    btn_submit.text = "FINISH"
                } else {
                    btn_submit.text = "GO TO NEXT QUESTION"
                }
                mSelectedOptionPosition = 0
            }
        }
    }

    /**
     * A function for setting the question to UI components.
     */
    @SuppressLint("SetTextI18n")
    private fun setQuestion() {
        val question = mQuestionsList!!.get(mCurrentPosition - 1) // Getting the question from the list with the help of current position.
        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {
            btn_submit.text = "FINISH"
        } else {
            btn_submit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        tv_question.text = question.question
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    /**
     * A function to set the view of selected option view.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = resources.getDrawable(R.drawable.selected_option_border_bg)
    }

    /**
     * A function to set default options view when the new question is loaded or when the answer is reselected.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = resources.getDrawable(R.drawable.default_option_border_bg)
        }
    }

    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_option_one.background = resources.getDrawable(drawableView)
            }
            2 -> {
                tv_option_two.background = resources.getDrawable(drawableView)
            }
            3 -> {
                tv_option_three.background = resources.getDrawable(drawableView)
            }
            4 -> {
                tv_option_four.background = resources.getDrawable(drawableView)
            }
        }
    }
}