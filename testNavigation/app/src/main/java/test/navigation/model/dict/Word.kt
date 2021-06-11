package test.navigation.model.dict

import com.google.firebase.database.Exclude


data class Word (
        val word: String,
        val phonetics: List<Phonetic>,
        val meanings: List<Meaning>,
        var isFavorite: Boolean = false
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "word" to word,
                "phonetics" to phonetics,
                "meanings" to meanings,
                "isFavorite" to isFavorite
        )
    }
}


data class Phonetic (
    val text: String,
    val audio: String
)

data class Meaning (
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition (
    val definition: String,
    val synonyms: List<String>? = null,
    val example: String? = null
)
