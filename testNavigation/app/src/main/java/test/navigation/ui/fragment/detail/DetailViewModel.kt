package test.navigation.ui.fragment.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import test.navigation.model.dict.Word

class DetailViewModel : ViewModel(){
    var wordData = MutableLiveData<Word>()
}