package test.navigation.store

import test.navigation.model.dict.Word
import test.navigation.model.question.CreateQuestion
import test.navigation.model.question.Question

object Account {
    var USER_ID: String = "user_id"
    var TOTAL_QUESTIONS: Int = 0
    var CORRECT_ANSWERS: Int = 0
    var wordList: ArrayList<Word>? = ArrayList()
    var userpool: String = ""
    fun getQuestions(numQuestion: Int): ArrayList<Question>? {
        return if(wordList.isNullOrEmpty()) null
        else {
            val questionsList=ArrayList<Question>()
            for (i in 0 until numQuestion) {
                questionsList.add(CreateQuestion.from(wordList!![i]))
            }
            questionsList
        }

    }
}