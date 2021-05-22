package test.navigation.networking.restDictionaryAPI

import test.navigation.dict.Word
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by nampham on 5/10/21.
 */
interface DictionaryService {
    @GET("entries/en_US/{word}")
    suspend fun listWordInformation(
        @Path("word") word: String
    ): ArrayList<Word>
}