package test.navigation.ui.fragment.quiz.question

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import test.navigation.model.dict.Word
import test.navigation.networking.restDictionaryAPI.RestClient
import test.navigation.store.Account

class QuestionViewModel : ViewModel(){
    private val questionResponse = MutableLiveData<ArrayList<Word>>()
    fun getWord(listPriKey: List<String>): LiveData<ArrayList<Word>> {
        for (priKey in listPriKey) {
            viewModelScope.launch {
                val wordResp = RestClient.getInstance().API.listWordInformation(
                        word = priKey
                )
                Log.e("TAG", wordResp[0].meanings.toString())
                Account.wordList?.add(wordResp[0])
                questionResponse.value?.add(wordResp[0])
            }
        }
        Log.i("QuestionViewModel: userPool", Account.wordList.toString())
        return questionResponse
    }
}