package test.navigation.ui.activity.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import test.navigation.R
import test.navigation.databinding.ActivityMainBinding
import test.navigation.model.dict.ParseWordList
import test.navigation.model.dict.Word
import test.navigation.model.dict.WordAdapter
import test.navigation.store.Account
import test.navigation.store.Account.wordList
import test.navigation.ui.fragment.quiz.question.QuestionFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    lateinit var questionFragment: QuestionFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //setContentView(R.layout.activity_main)

        val adapter = WordAdapter()
        binding.rcList.adapter = adapter
        adapter.data = wordList(ParseWordList)
    }

    private fun wordList(parseWordList: ParseWordList): List<Word> {
            return wordList(ParseWordList)
    }

}

//private fun ViewModelProvider.get(java: Class<MainViewModel>): MainViewModel {

//}
