package test.navigation.ui.fragment.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import test.navigation.model.dict.Word
import test.navigation.networking.restDictionaryAPI.RestClient

class RegisterViewModel : ViewModel() {
    var registerResponse: MutableLiveData<Word>?= null
    fun getWord(priKey: String): LiveData<Word>? {
        viewModelScope.launch {
            try {
                registerResponse = MutableLiveData()
                val wordResp = RestClient.getInstance().API.listWordInformation(
                        word = priKey
                )
                Log.e("TAG", wordResp.get(0).meanings.toString())
                registerResponse?.value = wordResp.get(0)
            } catch (ex: Exception) {
                ex.printStackTrace()
                if((ex as HttpException).code() == 404) {
                    registerResponse?.postValue(null)
                }
            }

        }
        return registerResponse
    }
}