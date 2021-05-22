package test.navigation.ui.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import test.navigation.R
import test.navigation.databinding.FragmentRegisterBinding
import test.navigation.dict.Userpool

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel(inflater, container)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSearch.setOnClickListener {
                if(!edtInputNewWord.text.isEmpty())
                    fetchData(edtInputNewWord.text.toString())
            }
            binding.progressBar.visibility = View.GONE
            btnRegister.setOnClickListener {
                if(registerViewModel.registerResponse.value?.meanings?.get(0).toString().isNotEmpty())
                    registerViewModel.registerResponse.value?.let { it1 ->
                        Userpool.wordList?.add(
                            it1
                        )
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
        binding.progressBar.visibility = View.VISIBLE

        registerViewModel.getWord(key).observe(viewLifecycleOwner, Observer {
            activity?.runOnUiThread {
                binding.progressBar.visibility = View.GONE
                binding.tvMeaning.text = it.meanings[0].toString()
            }
        })
    }
}