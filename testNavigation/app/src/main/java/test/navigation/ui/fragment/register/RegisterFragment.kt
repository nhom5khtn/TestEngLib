package test.navigation.ui.fragment.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_print.*
import test.navigation.R
import test.navigation.databinding.FragmentPrintBinding
import test.navigation.databinding.FragmentRegisterBinding
import test.navigation.model.dict.Word
import test.navigation.networking.database.DatabaseAPI
import test.navigation.ui.fragment.print.PrintViewModel


class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var newWord: Word? = null
    var valid: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSearch.setOnClickListener {
                if (edtInputNewWord.text.isNotEmpty()) {
                    edtInputNewWord.error = null
                    binding.progressBarRegister.visibility = View.VISIBLE
                    fetchData(edtInputNewWord.text.toString())
                } else {
                    edtInputNewWord.error = "Empty!"
                    valid = false
                }
            }
            binding.progressBarRegister.visibility = View.GONE
            setWidget(registerViewModel.registerResponse.value != null)
            btnRegister.setOnClickListener {
                if (edtInputNewWord.text.isNotEmpty() && valid) {
                    edtInputNewWord.error = null
                    registerViewModel.registerResponse.value?.let { wordResp ->
                        Log.e("add into userpool", wordResp.word)
                        val check = DatabaseAPI.addUserPool(wordResp)
                        if (!check) {
                            val dialog = AlertDialog.Builder(requireContext()).create()
                            dialog.setMessage("This word is exist in your pool")
                            dialog.setTitle("Duplicate error")
                            dialog.setButton(
                                Dialog.BUTTON_POSITIVE, "OK"
                            ) { dialogErr, _ -> dialogErr.dismiss() }
                            dialog.show()
                        }
                        else {
                            Log.e("add into userpool - fav", wordResp.isFavorite.toString())
                            val dialog = AlertDialog.Builder(requireContext()).create()
                            dialog.setMessage("Your word has been added to Pool!")
                            dialog.setTitle("Success")
                            dialog.setButton(
                                    Dialog.BUTTON_POSITIVE, "OK"
                            ) { dialogErr, _ -> dialogErr.dismiss() }
                            dialog.show()
                        }
                    }
                } else {
                    edtInputNewWord.error = "Empty!"
                    valid = false
                }
            }
        }
    }
    private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        binding.registerViewModelDataBinding = registerViewModel
    }

    private fun fetchData(key: String) {
        binding.progressBarRegister.visibility = View.VISIBLE

        registerViewModel.getWord(key).observe(viewLifecycleOwner, {
            activity?.runOnUiThread {
                binding.progressBarRegister.visibility = View.GONE
                if (it != null) {
                    setWidget(true)
                    newWord = it
                    valid = true
                } else {
                    setWidget(false)
                    newWord = null
                    valid = false
                    val dialog = AlertDialog.Builder(requireContext()).create()
                    dialog.setMessage("This word is wrong")
                    dialog.setTitle("Error")
                    dialog.setButton(
                            Dialog.BUTTON_POSITIVE, "OK"
                    ) { dialogErr, _ -> dialogErr.dismiss() }
                    dialog.show()
                }
            }
        })
    }
    @SuppressLint("SetTextI18n")
    fun setWidget(isNotNull: Boolean){
        binding.apply {
            if(!isNotNull){
                tvWordRegister.text         = ""
                tvPhoneticRegister.text     = ""
                tvPosRegister.text          = ""
                tvDefRegister.text          = ""
                tvExampleRegister.text      = ""
            } else {
                tvWordRegister.text         = registerViewModel.registerResponse.value?.word
                tvPhoneticRegister.text     = "Pronunciation: " + registerViewModel.registerResponse.value?.phonetics?.get(0)!!.text
                tvPosRegister.text          = registerViewModel.registerResponse.value?.meanings?.get(0)!!.partOfSpeech
                tvDefRegister.text          = registerViewModel.registerResponse.value?.meanings?.get(0)!!.definitions[0].definition
                tvExampleRegister.text      = "'" + registerViewModel.registerResponse.value?.meanings?.get(0)!!.definitions[0].example + "'"
            }
        }
    }
}