package test.navigation.ui.utils


object ParseStringToList {
    fun from(ListString: String): List<String>{
        val str = ListString.trim()
        return str.split(",")
    }
}