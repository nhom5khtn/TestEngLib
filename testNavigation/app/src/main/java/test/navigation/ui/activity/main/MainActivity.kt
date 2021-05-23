package test.navigation.ui.activity.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import test.navigation.R
import test.navigation.ui.fragment.quiz.question.QuestionFragment

class MainActivity : AppCompatActivity() {
    lateinit var questionFragment: QuestionFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}