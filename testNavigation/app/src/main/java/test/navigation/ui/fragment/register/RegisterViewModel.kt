package test.navigation.ui.fragment.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import test.navigation.model.dict.Word
import test.navigation.networking.restDictionaryAPI.RestClient

class RegisterViewModel : ViewModel() {
    val registerResponse = MutableLiveData<Word>()

    fun getWord(priKey: String): LiveData<Word> {
        viewModelScope.launch {
            val wordResp = RestClient.getInstance().API.listWordInformation(
                word = priKey
            )
            Log.e("TAG", wordResp.get(0).meanings.toString())
            registerResponse.value = wordResp.get(0)
        }
        return registerResponse
    }
}