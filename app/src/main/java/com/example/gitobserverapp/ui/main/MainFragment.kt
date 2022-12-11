package com.example.gitobserverapp.ui.main

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.R
import com.example.gitobserverapp.adapter.RepoSearchAdapter
import com.example.gitobserverapp.repository.network.model.Item
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment(), RepoSearchAdapter.Listener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val repoSearchAdapter: RepoSearchAdapter by lazy {
        RepoSearchAdapter(this)
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewInit()
        adapterListener()

        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)
            binding.progBarMain.visibility = View.VISIBLE

            mainViewModel.getRepos(binding.edtTxtInput.text.toString())
        }
    }

    private fun recyclerViewInit() {
        binding.recView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = repoSearchAdapter
    }

    private fun adapterListener() {
        mainViewModel.reposLiveData.observe(viewLifecycleOwner) { gitResponse ->
            binding.progBarMain.visibility = View.GONE
            if (gitResponse != null){
                repoSearchAdapter.differ.submitList(gitResponse.items)
            } else {
                Snackbar.make(binding.root, "No data from gitHub", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val hk = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        hk.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(item: Item) {
        findNavController().navigate(R.id.action_mainFragment_to_chartFragment)
    }
}