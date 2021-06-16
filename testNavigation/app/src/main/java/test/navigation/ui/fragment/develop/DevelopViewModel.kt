package test.navigation.ui.fragment.develop

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import test.navigation.store.Account
import test.navigation.ui.utils.ParseStringToList

class DevelopViewModel : ViewModel() {
    var arrayNumQuest   = MutableLiveData<Array<Int>>()
    var arrayNumCorrect = MutableLiveData<Array<Int>>()
    var arrayTimeStamp  = MutableLiveData<Array<String>>()

    init {
        Log.e("DevelopViewModel-Result", "${Account.RESULT}")
        Log.e("DevelopViewModel-cntList", Account.countList)
        val list = ParseStringToList.from(Account.countList)
        if (Account.RESULT.isNullOrEmpty()) {
            arrayNumQuest.value = arrayOf(0)
            arrayNumCorrect.value = arrayOf(0)
            arrayTimeStamp.value = arrayOf("")
        } else {
            val arrNumQuest     = Array(list.size){0}
            val arrNumCorrect   = Array(list.size){0}
            val arrTimeStamp    = Array(list.size){""}
            for (i in list.indices) {
                arrNumQuest[i]   = Account.RESULT[i]["numQuest"]?.toInt()!!
                arrNumCorrect[i] = Account.RESULT[i]["numCorrect"]?.toInt()!!
                arrTimeStamp[i]  = list[i].substring(0,list[i].length - 3)
            }
            arrayNumQuest.value   = arrNumQuest
            arrayNumCorrect.value = arrNumCorrect
            arrayTimeStamp.value = arrTimeStamp
            Log.e("DevelopViewModel-quest", "${arrayNumQuest.value}")
            Log.e("DevelopViewModel-cntList", "${arrayNumCorrect.value}")
        }
    }
}