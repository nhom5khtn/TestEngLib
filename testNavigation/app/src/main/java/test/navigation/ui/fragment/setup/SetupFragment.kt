package test.navigation.ui.fragment.setup

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import test.navigation.R
import test.navigation.databinding.FragmentSetupBinding
import test.navigation.store.Account

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

        Log.i("Setup: userPool", Account.wordList.toString())
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


        val savedWaitTime = saveConfig?.getString("WAIT_TIME","10.0F")
        val savedNumQuest = saveConfig?.getString("NUM_QUEST","30.0F")
        saveConfig?.edit {
            putString("WAIT_TIME",waitTime.value.toString())
            putString("NUM_QUEST",numQuest.value.toString())
        }
        Log.e(">>>>> savedWaitTime","$savedWaitTime")
        Log.e(">>>>> savedNumQuest","$savedNumQuest")
        if (savedWaitTime != null) {
            binding.sWaitTime.setValue(savedWaitTime.toFloat())
        }
        if (savedNumQuest != null) {
            binding.sNumQuest.setValue(savedNumQuest.toFloat())
        }

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