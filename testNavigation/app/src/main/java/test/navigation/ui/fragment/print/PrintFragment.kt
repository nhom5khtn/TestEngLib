package test.navigation.ui.fragment.print

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_print.*
import test.navigation.R
import test.navigation.databinding.FragmentPrintBinding
import test.navigation.model.dict.Word
import test.navigation.store.Account

class PrintFragment : Fragment() {
    private lateinit var printAdapter: PrintAdapter
    lateinit var binding: FragmentPrintBinding
    private lateinit var printViewModel: PrintViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        setupViewModel(inflater, container)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchData()
    }

    private fun setupRecyclerView() {
        Log.d("Print", "setupRecycler")
        printAdapter = PrintAdapter()
        printAdapter.listener = object : PrintAdapter.PreCachingLayoutManager.WordItemListener {
            override fun onItemClicked(word: Word) {
                ViewCompat.postOnAnimationDelayed(view!!, // Delay to show ripple effect
                        {
                            val bundle = bundleOf("word" to word)
                            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
                        }, 50)
                Log.e("click item", " show detail")
            }
        }
        printAdapter.favListener = object : PrintAdapter.PrintAdapterListener {
            override fun onItemClicked(word: Word) {
                printViewModel.toggleFavoriteWord(word)
            }
        }
        rv_userpool.layoutManager = LinearLayoutManager(activity)
        rv_userpool.adapter = printAdapter
    }

    private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        printViewModel = ViewModelProvider(this).get(PrintViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_print, container, false)
        binding.lifecycleOwner = this
        binding.printViewModelDataBinding = printViewModel
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        Log.e("list in printAdapter", Account.wordList.toString())
        printViewModel.listData.observe(viewLifecycleOwner, printAdapter::submitList)
        binding.progressBar.visibility = View.GONE
    }
}