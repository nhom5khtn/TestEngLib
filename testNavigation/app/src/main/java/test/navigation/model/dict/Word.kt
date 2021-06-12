package test.navigation.model.dict

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word (
        val word: String,
        val phonetics: List<Phonetic>,
        val meanings: List<Meaning>,
        var isFavorite: Boolean = false
) : Parcelable {
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


@Parcelize
data class Phonetic (
    val text: String,
    val audio: String
) : Parcelable

@Parcelize
data class Meaning (
    val partOfSpeech: String,
    val definitions: List<Definition>
) : Parcelable

@Parcelize
data class Definition (
    val definition: String,
    val synonyms: List<String>? = null,
    val example: String? = null
) : Parcelable
