package test.navigation.ui.fragment.setup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import test.navigation.R
import test.navigation.databinding.FragmentSetupBinding

class SetupFragment: Fragment() {

    private var _binding: FragmentSetupBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SetupViewModel

    private lateinit var viewModelFactory: SetupViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sWaitTime.value = 10.0F
        binding.sNumQuest.value = 30.0F

        val waitTime = binding.sWaitTime
        //viewModel.setConfigurationWaitTimeQuest(waitTime)
        val numQuest = binding.sNumQuest
        //viewModel.setConfigurationNumberQuests(numQuest)
        viewModelFactory = SetupViewModelFactory(waitTime,numQuest)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SetupViewModel::class.java)


        val saveConfig = this.activity?.getSharedPreferences("CONFIGURATION", Context.MODE_PRIVATE)

        saveConfig?.edit {
            putString("DEFAULT_WAIT_TIME","10.0F")
            putString("DEFAULT_NUM_QUEST","30.0F")
        }

        /*
        Changes to a wait time slider can be observed
        */
//        val _waitTime = binding.sWaitTime.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
//            override fun onStartTrackingTouch(slider: Slider) {
//                // Responds to when slider's touch event is being started
//            }
//
//            override fun onStopTrackingTouch(slider: Slider) {
//                // Responds to when slider's touch event is being stopped
//            }
//        })

//        binding.sWaitTime.addOnChangeListener { slider, value, fromUser ->
//            // Responds to when slider's value is changed
//        }

        /*
        Changes to a num quest slider can be observed
        */
//        val _numQuest = binding.sNumQuest.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
//            override fun onStartTrackingTouch(slider: Slider) {
//                // Responds to when slider's touch event is being started
//            }
//
//            override fun onStopTrackingTouch(slider: Slider) {
//                // Responds to when slider's touch event is being stopped
//            }
//        })

//        binding.sNumQuest.addOnChangeListener { slider, value, fromUser ->
//            // Responds to when slider's value is changed
//        }


        binding.apply {

            btnSaveSetup.setOnClickListener{
                viewModel.setConfigurationWaitTimeQuest(waitTime)
                viewModel.setConfigurationNumberQuests(numQuest)
                saveConfig?.edit {
                    putString("WAIT_TIME",waitTime.value.toString())
                    putString("NUM_QUEST",numQuest.value.toString())
                }
            }

            btnDefault.setOnClickListener{
                val defaultWaitTime = saveConfig?.getString("DEFAULT_WAIT_TIME","10.0F")
                val defaultNumQuest = saveConfig?.getString("DEFAULT_NUM_QUEST","30.0F")
                saveConfig?.edit {
                    putString("DEFAULT_WAIT_TIME",defaultWaitTime)
                    putString("DEFAULT_NUM_QUEST",defaultNumQuest)
                }
                if (defaultWaitTime != null) {
                    sWaitTime.value = defaultWaitTime.toFloat()
                }
                if (defaultNumQuest != null) {
                    sNumQuest.value = defaultNumQuest.toFloat()
                }

            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}