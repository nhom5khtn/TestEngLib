package test.navigation.store

import test.navigation.model.dict.Word

class Userpool private constructor() {
    companion object {
        var wordList: ArrayList<Word>?=null
    }
}