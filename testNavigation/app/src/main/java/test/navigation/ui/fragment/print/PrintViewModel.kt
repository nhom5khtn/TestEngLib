package test.navigation.ui.fragment.print

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import test.navigation.dict.Word
import test.navigation.networking.restDictionaryAPI.RestClient

class PrintViewModel: ViewModel() {
    val isLinearSwitched = MutableLiveData(true)
    private val printResponse = MutableLiveData<List<Word>>()

    fun getWord(priKey: String): LiveData<List<Word>> {
        viewModelScope.launch {
            val wordResp = RestClient.getInstance().API.listWordInformation(
                word = priKey
            )
            Log.e("TAG", wordResp.get(0).meanings.toString())
            printResponse.value = wordResp
        }
        return printResponse
    }
}