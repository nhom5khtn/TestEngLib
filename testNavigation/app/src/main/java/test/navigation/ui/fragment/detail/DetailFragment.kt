package test.navigation.ui.fragment.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_detail.*
import test.navigation.R
import test.navigation.databinding.FragmentDetailBinding
import test.navigation.model.dict.Word
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var defAdapter: DefAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setupViewModel(inflater,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
        fetchData()
    }
    private fun setupViewModel(inflater: LayoutInflater,container: ViewGroup?){
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        binding.detailViewModel = viewModel
    }

    private fun setupRecyclerView() {
        Log.d("Detail", "setupRecycler")
        defAdapter = DefAdapter()
        rv_meanings.layoutManager = LinearLayoutManager(activity)
        rv_meanings.adapter = defAdapter

    }
    private fun showDetailView(){
        binding.apply {
            tvTitleWord.text = viewModel.wordData.value!!.word
            tvPhonetics.text = viewModel.wordData.value!!.phonetics[0].text
            tbHeart.isChecked = viewModel.wordData.value!!.isFavorite
            tbHeart.setOnCheckedChangeListener { _, isChecked ->
                viewModel.wordData.value!!.isFavorite = isChecked
                if (isChecked) {
                    Log.e("heart", "stored")
                    Log.e("heart", Account.favUserPool)
                    DatabaseAPI.clicked(viewModel.wordData.value!!.word)
                } else {
                    Log.e("heart", "not stored")
                    Log.e("heart", Account.favUserPool)
                    DatabaseAPI.unClicked(viewModel.wordData.value!!.word)
                }
            }
        }

    }

    private fun getDataFromBundle(): Word? {
        return arguments?.getParcelable("word")
    }

    private fun fetchData() {
        val data = getDataFromBundle()
        data?.let {
            viewModel.wordData.value = data
            Log.e("list in detailAdapter", viewModel.wordData.value!!.meanings.toString())
            defAdapter.setList(viewModel.wordData.value!!.meanings)
            showDetailView()
        }
    }
}