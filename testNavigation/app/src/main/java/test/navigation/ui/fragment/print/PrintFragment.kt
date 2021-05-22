package test.navigation.ui.fragment.print

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import test.navigation.R
import test.navigation.databinding.FragmentPrintBinding
import test.navigation.dict.Userpool
import test.navigation.dict.Word

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
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        fetchData()
    }

    private fun setupRecyclerView() {
        Log.d("NowPlaying", "setupRecycler")
        printAdapter = PrintAdapter()
        printAdapter.listener = object : PrintAdapter.MovieItemListener {
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
        binding.rvUserpool.adapter = printAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("Print", "onCreateOption")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
        this.menu = menu
        if (printViewModel.isLinearSwitched.value!!) {
            menu[0].icon = ContextCompat.getDrawable(requireActivity(), R.drawable.icon_grid)
        } else {
            menu[0].icon = ContextCompat.getDrawable(requireActivity(), R.drawable.icon_linear)
        }
    }

    private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        printViewModel = ViewModelProvider(this).get(PrintViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_print, container, false)
        binding.lifecycleOwner = this
        binding.printViewModelDataBinding = printViewModel
    }

    private fun fetchData() {
        printAdapter.submitList(Userpool.wordList)
        binding.progressBar.visibility = View.GONE
    }
}