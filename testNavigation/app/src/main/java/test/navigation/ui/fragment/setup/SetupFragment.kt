package test.navigation.ui.fragment.setup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.slider.Slider
import test.navigation.R
import test.navigation.databinding.FragmentSetupBinding
import test.navigation.store.Account

class SetupFragment: Fragment() {

    private lateinit var binding: FragmentSetupBinding

    private lateinit var viewModel: SetupViewModel

    private lateinit var viewModelFactory: SetupViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setup,
            container,
            false
        )

        var waitTime: Slider? = binding.sWaitTime   // waittime slider ref
        var numQuest: Slider? = binding.sNumQuest   //numquest slider ref

        Log.i("Setup: userPool", Account.wordList.toString())
        Log.i("Setup: userID", Account.USER_ID)

        // init shared preferences with default config keys
        val saveConfig = this.activity?.getSharedPreferences("CONFIGURATION_"+Account.USER_ID, Context.MODE_PRIVATE)
        saveConfig?.edit {
            putString("DEFAULT_WAIT_TIME", "10.0F")
            putString("DEFAULT_NUM_QUEST", "30.0F")
        }

        // get shared preferences with valid config keys
        val savedWaitTime = saveConfig?.getString("WAIT_TIME", "10.0F")
        val savedNumQuest = saveConfig?.getString("NUM_QUEST", "30.0F")

        // check valid config key: null -> default value, !null -> valid value
        binding.apply {
            if (savedWaitTime == null || savedNumQuest == null) {
                val defaultWaitTime = saveConfig?.getString("DEFAULT_WAIT_TIME", "10.0F")
                val defaultNumQuest = saveConfig?.getString("DEFAULT_NUM_QUEST", "30.0F")
                saveConfig?.edit {
                    putString("DEFAULT_WAIT_TIME", defaultWaitTime)
                    putString("DEFAULT_NUM_QUEST", defaultNumQuest)
                }
                if (defaultWaitTime != null) {
                    sWaitTime.value = defaultWaitTime.toFloat()
                }
                if (defaultNumQuest != null) {
                    sNumQuest.value = defaultNumQuest.toFloat()
                }
            } else {
                if (savedWaitTime != null) {
                    if (savedNumQuest != null) {
                        //setupConfig(waitTime, numQuest)
                        binding.sWaitTime.value = savedWaitTime.toFloat()
                        binding.sNumQuest.value = savedNumQuest.toFloat()
                    }
                }
            }
        }

        viewModelFactory = SetupViewModelFactory(binding.sWaitTime, binding.sNumQuest)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SetupViewModel::class.java)
        binding.configurarion = viewModel.configuration.value

        viewModel.configuration.observe(viewLifecycleOwner, {
            binding.configurarion?.numQuest = it.numQuest
            binding.configurarion?.waitTimeQuest = it.waitTimeQuest
        })

        binding.apply {
            btnSaveSetup.setOnClickListener {
                if (waitTime != null) {
                    viewModel.setConfigurationWaitTimeQuest(waitTime)
                }
                if (numQuest != null) {
                    viewModel.setConfigurationNumberQuests(numQuest)
                }
                saveConfig?.edit {
                    putString("WAIT_TIME", waitTime?.value.toString())
                    putString("NUM_QUEST", numQuest?.value.toString())
                }
            }
            btnDefault.setOnClickListener {
                val defaultWaitTime = saveConfig?.getString("DEFAULT_WAIT_TIME", "10.0F")
                val defaultNumQuest = saveConfig?.getString("DEFAULT_NUM_QUEST", "30.0F")
                saveConfig?.edit {
                    putString("DEFAULT_WAIT_TIME", defaultWaitTime)
                    putString("DEFAULT_NUM_QUEST", defaultNumQuest)
                }
                if (defaultWaitTime != null) {
                    sWaitTime.value = defaultWaitTime.toFloat()
                }
                if (defaultNumQuest != null) {
                    sNumQuest.value = defaultNumQuest.toFloat()
                }

            }
        }

        return binding.root
    }

//    private fun setupConfig(waitTime: Slider, numQuest: Slider) {
//        // shared preferences with default config keys
//        val saveConfig = this.activity?.getSharedPreferences("CONFIGURATION", Context.MODE_PRIVATE)
//        saveConfig?.edit {
//            putString("DEFAULT_WAIT_TIME", "10.0F")
//            putString("DEFAULT_NUM_QUEST", "30.0F")
//        }
//        // test for showing data
//        val savedWaitTime = saveConfig?.getString("WAIT_TIME", "10.0F")
//        val savedNumQuest = saveConfig?.getString("NUM_QUEST", "30.0F")
//        saveConfig?.edit {
//            putString("WAIT_TIME", waitTime.value.toString())
//            putString("NUM_QUEST", numQuest.value.toString())
//        }
//        Log.e(">>>>> savedWaitTime", "$savedWaitTime")
//        Log.e(">>>>> savedNumQuest", "$savedNumQuest")
//        if (savedWaitTime != null) {
//            binding.sWaitTime.value = savedWaitTime.toFloat()
//        }
//        if (savedNumQuest != null) {
//            binding.sNumQuest.value = savedNumQuest.toFloat()
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val saveConfig = this.activity?.getSharedPreferences("CONFIGURATION_"+Account.USER_ID, Context.MODE_PRIVATE)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.configurarion = null
    }

}




