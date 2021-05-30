package test.navigation.ui.fragment.quiz.question

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_question.*
import test.navigation.R
import test.navigation.databinding.FragmentQuestionBinding
import test.navigation.model.question.Question
import test.navigation.store.Account
import test.navigation.ui.activity.main.MainActivity


class QuestionFragment : Fragment() {

    val TAG = MainActivity::class.java.simpleName
    private var mCurrentPosition: Int = 0 // Default and the first question position
    private var mQuestionsList: ArrayList<Question>? = null
    private var mCorrectAnswers: Int = 0
    private var mSelectedOptionPosition: Int = 0
    lateinit var questionViewModel: QuestionViewModel
    lateinit var binding: FragmentQuestionBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        setupViewModel(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //===================================================================================================
        val saveConfig = activity?.getSharedPreferences("CONFIGURATION", Context.MODE_PRIVATE)

        saveConfig?.edit {
            putString("DEFAULT_WAIT_TIME", "10.0F")
            putString("DEFAULT_NUM_QUEST", "30.0F")
        }

        val savedWaitTime = saveConfig?.getString("WAIT_TIME", "10.0F")
        val savedNumQuest = saveConfig?.getString("NUM_QUEST", "30.0F")
        Log.e(">>>>> savedWaitTime", "$savedWaitTime")
        Log.e(">>>>> savedNumQuest", "$savedNumQuest")
        if (savedWaitTime != null) {
            var WaitTime = saveConfig.getString("DEFAULT_WAIT_TIME", "10.0F")
            Log.e(">>>>> savedWaitTime", "$WaitTime")
        }
        if (savedNumQuest != null) {
            var NumQuest = saveConfig.getString("DEFAULT_NUM_QUEST", "30.0F")
            Log.e(">>>>> savedNumQuest", "$NumQuest")
        }
        //===================================================================================================

        Log.i("Quest Userpool: ", Account.wordList.toString())


        if (savedNumQuest != null) {
            mQuestionsList = if (Account.wordList!!.size >= savedNumQuest.toFloat().toInt())
                Account.getQuestions(savedNumQuest.toFloat().toInt())
            else
                Account.getQuestions(Account.wordList!!.size)
        }


        progressBar.max = mQuestionsList?.size!!
        setQuestion()

            tv_option_one.setOnClickListener {
                if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                selectedOptionView(tv_option_one, 1)
                }
                else{
                    //do nothing
                }
            }
            tv_option_two.setOnClickListener {
                if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                    selectedOptionView(tv_option_two, 2)
                }
                else{
                    //do nothing
                }
            }
            tv_option_three.setOnClickListener {
                if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                    selectedOptionView(tv_option_three, 3)
                }
                else{
                    //do nothing
                }
            }
            tv_option_four.setOnClickListener {
                if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                    selectedOptionView(tv_option_four, 4)
                }
                else{
                    //do nothing
                }

            }


        btn_submit.setOnClickListener {
            //if the user don't choose any answer
            if (mSelectedOptionPosition == 0 || btn_submit.text == "GO TO NEXT QUESTION" || btn_submit.text == "FINISH") {
                mCurrentPosition++
                when {
                    mCurrentPosition  < (mQuestionsList!!.size)-> {
                        setQuestion()
                    }
                    else -> {
                        Account.CORRECT_ANSWERS = mCorrectAnswers
                        Account.TOTAL_QUESTIONS = mQuestionsList!!.size
                        findNavController().navigate(R.id.action_questionFragment_to_resultFragment)
                    }
                }
            }
            // if the user has chosen, the app will progress the answer
            else {

                    val question = mQuestionsList?.get(mCurrentPosition)

                    // This is to check if the answer is wrong

                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }
                // This is result view
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    tv_option_one.linksClickable = false
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
            tv_progress.text = mCurrentPosition.toString() + "/" + mQuestionsList?.size!!
            Log.e("tv_progress.text", "$tv_progress.text")
            val question = mQuestionsList!![mCurrentPosition] // Getting the question from the list with the help of current position.
            defaultOptionsView()

            /*if (mCurrentPosition == mQuestionsList!!.size+1) {
                btn_submit.text = "FINISH"
            } else {
                btn_submit.text = "SUBMIT"
            }*/
			btn_submit.text = "SUBMIT"
            progressBar.progress = mCurrentPosition
            var mCurrentPositionToShow: Int = mCurrentPosition +1
            tv_progress.text = "$mCurrentPositionToShow" + "/" + progressBar.max

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

        private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
            questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)
            binding.lifecycleOwner = this
            binding.questionViewModelDataBinding = questionViewModel
        }
    }
