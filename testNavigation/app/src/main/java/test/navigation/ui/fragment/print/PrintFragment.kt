package test.navigation.ui.fragment.print

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var menu: Menu

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

    override fun onResume() {
        super.onResume()
//        if (printAdapter.currentList != Account.wordList)
//            printAdapter.submitList(Account.wordList)
        printAdapter.notifyDataSetChanged()
    }
    private fun setupRecyclerView() {
        Log.d("Print", "setupRecycler")
        printAdapter = PrintAdapter()
        printAdapter.listener = object : PrintAdapter.WordItemListener {
            override fun onItemClicked(word: Word) {
//                ViewCompat.postOnAnimationDelayed(view!!, // Delay to show ripple effect
//                    Runnable {
//                        val bundle = bundleOf("movie" to movie)
//                        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
//                    }
//                    ,50)
                Log.e("click item", "dgwg")
            }
        }
        rv_userpool.layoutManager = LinearLayoutManager(activity)
        rv_userpool.adapter = printAdapter

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        Log.d("Print", "onCreateOption")
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu_home, menu)
//        this.menu = menu
//        if (printViewModel.isLinearSwitched.value!!) {
//            menu[0].icon = ContextCompat.getDrawable(requireActivity(), R.drawable.icon_grid)
//        } else {
//            menu[0].icon = ContextCompat.getDrawable(requireActivity(), R.drawable.icon_linear)
//        }
//    }

    private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        printViewModel = ViewModelProvider(this).get(PrintViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_print, container, false)
        binding.lifecycleOwner = this
        binding.printViewModelDataBinding = printViewModel
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        Log.e("list in printAdapter", Account.wordList.toString())
        printAdapter.submitList(Account.wordList)
        binding.progressBar.visibility = View.GONE
    }
}