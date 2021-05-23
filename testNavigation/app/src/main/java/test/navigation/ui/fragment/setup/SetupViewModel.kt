package test.navigation.ui.fragment.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.slider.Slider
import test.navigation.model.configuration.Configuration

class SetupViewModel(waitTimeQuest: Slider, numQuest: Slider) : ViewModel(){

    var configuration : MutableLiveData<Configuration> = MutableLiveData()

    init {
        configuration.value = Configuration(waitTimeQuest,numQuest)
    }

    fun setConfigurationWaitTimeQuest(waitTimeQuest: Slider){
        configuration.value?.waitTimeQuest=waitTimeQuest
        configuration.postValue(configuration.value)
    }

    fun setConfigurationNumberQuests(numQuest: Slider){
        configuration.value?.numQuest=numQuest
        configuration.postValue(configuration.value)
    }

}