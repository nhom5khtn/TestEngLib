package test.navigation.model.question

import test.navigation.model.dict.Word

object CreateQuestion {
    fun from(word: Word): Question{
        val optionOne: String = ""
        val optionTwo: String = ""
        val optionThree: String = ""
        val optionFour: String = ""
        val correctAnswer = if (optionOne == word.word) 1 else if (optionTwo == word.word) 2  else if (optionThree == word.word) 3  else 4
        return Question(0, word.meanings[0].definitions[0].definition, optionOne, optionTwo, optionThree, optionFour, correctAnswer)
    }
}