package test.navigation.model.dict

object ParseWordList {
    fun from(wordListString: String): List<String>{
        val str = wordListString.trim()
        var wordList: List<String> = str.split(",")
        return wordList
    }
}