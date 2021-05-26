package test.navigation.ui.fragment.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import test.navigation.R
import test.navigation.databinding.FragmentRegisterBinding
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account


class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
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
                if(edtInputNewWord.text.isNotEmpty())
                    fetchData(edtInputNewWord.text.toString())
            }
            binding.progressBarRegister.visibility = View.GONE
            btnRegister.setOnClickListener {
                if(registerViewModel.registerResponse.value?.meanings?.get(0).toString().isNotEmpty())
                    registerViewModel.registerResponse.value?.let { it1 ->
                        Log.e("add into userpool", it1.word)
                        DatabaseAPI.addUserPool(it1)
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
                binding.tvMeaning.text = it.meanings[0].toString()
            }
        })
    }
}