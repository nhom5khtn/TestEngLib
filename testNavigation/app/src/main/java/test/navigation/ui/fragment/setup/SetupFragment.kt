package test.navigation.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

        val waitTime: Slider = binding.sWaitTime   // waittime slider ref
        val numQuest: Slider = binding.sNumQuest   //numquest slider ref

        Log.i("Setup: userPool", Account.wordList.toString())
        Log.i("Setup: userID", Account.USER_ID)

        // init shared preferences with default config keys
        val saveConfig = this.activity?.getSharedPreferences("CONFIGURATION", Context.MODE_PRIVATE)
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
                //setupConfig(waitTime, numQuest)
                binding.sWaitTime.value = savedWaitTime.toFloat()
                binding.sNumQuest.value = savedNumQuest.toFloat()
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
                viewModel.setConfigurationWaitTimeQuest(waitTime)
                viewModel.setConfigurationNumberQuests(numQuest)
                saveConfig?.edit {
                    putString("WAIT_TIME", waitTime.value.toString())
                    putString("NUM_QUEST", numQuest.value.toString())
                }
                val dialog = AlertDialog.Builder(requireContext()).create()
                dialog.setMessage("Save completely")
                dialog.setTitle("Notification")
                dialog.setButton(
                        Dialog.BUTTON_POSITIVE, "OK"
                ) { dialogErr, _ -> dialogErr.dismiss() }
                dialog.show()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val saveConfig = this.activity?.getSharedPreferences("CONFIGURATION_"+Account.USER_ID, Context.MODE_PRIVATE)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.configurarion = null
    }

}
