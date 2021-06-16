package test.navigation.ui.fragment.quiz.question

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_question.*
import test.navigation.R
import test.navigation.databinding.FragmentQuestionBinding
import test.navigation.model.question.Question
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account
import test.navigation.ui.utils.PlayAudioManager

class QuestionFragment : Fragment() {

    private var mCurrentPosition: Int = 0 // Default and the first question position
    private var mQuestionsList: ArrayList<Question>? = null
    private var mCorrectAnswers: Int = 0
    private var mSelectedOptionPosition: Int = 0
    lateinit var binding: FragmentQuestionBinding
    private var numQuest: Int = 0
    private var waitTime: Int = 0


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        setupViewModel(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("PrepareQuestionFragment-Result", "${Account.RESULT}")
        Log.e("PrepareQuestionFragment-cntList", Account.countList)
        DatabaseAPI.loadResult()
        //===================================================================================================
        val saveConfig = activity?.getSharedPreferences("CONFIGURATION", Context.MODE_PRIVATE)
        saveConfig?.edit {
            putString("DEFAULT_WAIT_TIME", "10.0F")
            putString("DEFAULT_NUM_QUEST", "30.0F")
        }
        waitTime = saveConfig?.getString("WAIT_TIME", "10.0F")?.toFloat()!!.toInt()
        numQuest = saveConfig.getString("NUM_QUEST", "30.0F")?.toFloat()!!.toInt()
        Log.e(">>>>> savedWaitTime", "$waitTime")
        Log.e(">>>>> savedNumQuest", "$numQuest")
        //===================================================================================================

        Log.i("Quest Userpool: ", Account.wordList.toString())


        mQuestionsList = if (Account.wordList!!.size >= numQuest)
            Account.getQuestions(numQuest)
        else
            Account.getQuestions(Account.wordList!!.size)


        progressBar.max = mQuestionsList?.size!!
        setQuestion()

        tv_option_one.setOnClickListener {
            if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                selectedOptionView(tv_option_one, 1)
            }
        }
        tv_option_two.setOnClickListener {
            if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                selectedOptionView(tv_option_two, 2)
            }
        }
        tv_option_three.setOnClickListener {
            if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                selectedOptionView(tv_option_three, 3)
            }
        }
        tv_option_four.setOnClickListener {
            if(btn_submit.text != "GO TO NEXT QUESTION" && btn_submit.text != "FINISH") {
                selectedOptionView(tv_option_four, 4)
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
                        PlayAudioManager.stopAudio()
                        findNavController().navigate(R.id.action_questionFragment_to_resultFragment)
                    }
                }
            }
            // if the user has chosen, the app will progress the answer
            else {

                val question = mQuestionsList?.get(mCurrentPosition)

                // This is to check if the answer is wrong

                if (question!!.correctAnswer != mSelectedOptionPosition) {
                    PlayAudioManager.playAudioFromRaw(context, "wrong")
                    answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                } else {
                    PlayAudioManager.playAudioFromRaw(context, "right")
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

        btn_submit.text = "SUBMIT"
        progressBar.progress = mCurrentPosition+1
        val mCurrentPositionToShow: Int = mCurrentPosition +1
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
        tv.background = ContextCompat.getDrawable(requireActivity(), R.drawable.selected_option_border_bg)
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
            option.background = ContextCompat.getDrawable(requireActivity(), R.drawable.default_option_border_bg)

        }
    }

    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(requireActivity(), drawableView)
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(requireActivity(), drawableView)
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(requireActivity(), drawableView)
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(requireActivity(), drawableView)
            }
        }
    }

    private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)
    }

}