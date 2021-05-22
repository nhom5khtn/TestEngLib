package test.navigation.networking.restDictionaryAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by nampham on 5/10/21.
 */
class RestClient {
    private var api : DictionaryService

    val API : DictionaryService
        get() = api

    init {
        api = createDictionaryService()
    }

    companion object {
        private var mInstance: RestClient? = null
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"
        fun getInstance() = mInstance ?: synchronized(this){
            mInstance ?: RestClient().also { mInstance = it }
        }
    }

    private fun createDictionaryService() : DictionaryService {
//        //create okhttp
//        val httpClient = OkHttpClient.Builder()
//            .addInterceptor(AuthenticationInterceptor())
//            .build()

        //create retrofit
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(httpClient)
            .build()

        return retrofit.create(DictionaryService::class.java)
    }
}