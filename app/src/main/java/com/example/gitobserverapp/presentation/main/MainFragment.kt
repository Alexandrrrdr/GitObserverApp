package com.example.gitobserverapp.presentation.main

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.presentation.InternetConnection
import com.example.gitobserverapp.presentation.main.main_helper.ViewState

class MainFragment : Fragment(), RepoSearchAdapter.Listener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val internet = InternetConnection()

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
        renderUi()
        initViewStatement()
        checkInternet()
    }

    private fun checkInternet(){
        binding.btnSearch.setOnClickListener {
            if (internet.checkInternetConnection(requireContext())) {
                hideKeyboard(it)
                binding.progBarMain.visibility = View.VISIBLE
                if (binding.edtTxtInput.text.isNullOrEmpty()) {
                    mainViewModel.setStatement("Search field is empty")
                    mainViewModel.setReposList(null)
                } else {
                    mainViewModel.getRepos(binding.edtTxtInput.text.toString())
                }
            } else {
                mainViewModel.setStatement("Check Internet connection")
                mainViewModel.setReposList(null)
            }
        }
    }

    private fun recyclerViewInit() {
        binding.recView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = repoSearchAdapter
    }

    private fun initViewStatement() {
        mainViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Error -> {
                    binding.progBarMain.visibility = View.GONE
                    binding.txtError.visibility = View.VISIBLE
                    binding.txtError.text = viewState.error
                }
                ViewState.Loading -> {
                    binding.progBarMain.visibility = View.VISIBLE
                    binding.txtError.visibility = View.GONE
                }
                is ViewState.ViewContentMain -> {
                    binding.progBarMain.visibility = View.GONE
                    binding.txtError.visibility = View.GONE
                    mainViewModel.setReposList(viewState.result)
                }
            }
        }
    }

    private fun renderUi() {
        mainViewModel.reposLiveData.observe(viewLifecycleOwner) { gitResponse ->
            binding.progBarMain.visibility = View.GONE
            repoSearchAdapter.differ.submitList(gitResponse)
        }
    }

    private fun hideKeyboard(view: View) {
        val hk = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        hk.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onClick(item: MainModel) {
        val repoCreatedAt: String = item.repoCreated
        val repoOwnerLogin: String = item.repoOwnerName
        val repoName: String = item.repoName
        val direction = MainFragmentDirections.actionMainFragmentToChartFragment(repoName = repoName, repoOwnerName = repoOwnerLogin, repoCreated = repoCreatedAt)

        findNavController().navigate(directions = direction)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}