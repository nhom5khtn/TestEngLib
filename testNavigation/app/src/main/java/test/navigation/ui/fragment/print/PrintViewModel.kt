package test.navigation.ui.fragment.print

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.slider.Slider
import test.navigation.model.configuration.Configuration
import test.navigation.model.dict.Word
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account

class PrintViewModel: ViewModel() {
    var listData: MutableLiveData<ArrayList<Word>> = MutableLiveData()

    init {
        listData.value = Account.wordList
    }

    fun toggleFavoriteWord(word: Word) {
        if (word.isFavorite) {
            Log.e("heart", "not stored")
            DatabaseAPI.unClicked(word.word)
        } else {
            Log.e("heart", "stored")
            DatabaseAPI.clicked(word.word)
        }
    }
}