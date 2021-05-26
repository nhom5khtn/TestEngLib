package test.navigation.model.dict


object ParseWordList {
    fun from(wordListString: String): List<String>{
        val str = wordListString.trim()
        return str.split(",")
    }
}