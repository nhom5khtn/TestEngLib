package test.navigation.ui.fragment.register

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.word_view.view.*
import test.navigation.R
import test.navigation.databinding.FragmentRegisterBinding
import test.navigation.model.dict.Word
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account


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
           /*btnSearch.setOnClickListener {
                if(edtInputNewWord.text.isNotEmpty()){
                    fetchData(edtInputNewWord.text.toString())
                    valid = true
                }
            }*/
            btnSearch.setOnClickListener {
                if (!edtInputNewWord.text.isEmpty()) {
                    edtInputNewWord.error = null
                    binding.progressBarRegister.visibility = View.VISIBLE
                    fetchData(edtInputNewWord.text.toString())

                } else {
                    edtInputNewWord.error = "Empty!"
                    valid = false
                }

            }
            binding.progressBarRegister.visibility = View.GONE
            btnRegister.setOnClickListener {
               // if (valid == true){
                        registerViewModel.registerResponse?.value?.let { it ->
                            Log.e("add into userpool", it.word)
                            DatabaseAPI.addUserPool(it)
                        }
                //}

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
       /* binding.progressBarRegister.visibility = View.VISIBLE

        registerViewModel.getWord(key).observe(viewLifecycleOwner, {
            activity?.runOnUiThread {
                binding.progressBarRegister.visibility = View.GONE
                binding.tvMeaning.text = it.meanings[0].toString()
            }
        })*/
           registerViewModel.getWord(key)?.observe(viewLifecycleOwner, Observer {
               binding.progressBarRegister.visibility = View.GONE
               if (it != null) {
                   binding.tvMeaning.text = it.meanings[0].definitions[0].definition
                   newWord = it
                   valid = true
               } else {
                   newWord = null
                    valid = false
                   val dialog = AlertDialog.Builder(requireContext()).create()
                   dialog.setMessage("This word is wrong")
                   dialog.setTitle("Error")
                   dialog.setButton(
                           Dialog.BUTTON_POSITIVE, "OK"
                   ) { dialog, _ ->
                       dialog.dismiss()
                   }
                   dialog.show()
               }

           //    registerViewModel.registerResponse = null
           })
       }

    }
