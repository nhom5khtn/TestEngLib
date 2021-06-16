package test.navigation.ui.activity.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import test.navigation.R
import test.navigation.store.Account
import test.navigation.ui.fragment.quiz.question.QuestionFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MainActivity-cntList", Account.countList)
        setContentView(R.layout.activity_main)
    }
}