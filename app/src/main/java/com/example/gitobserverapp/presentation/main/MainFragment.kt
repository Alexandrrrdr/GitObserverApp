package com.example.gitobserverapp.presentation.main

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.App
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.presentation.main.main_helper.MainViewState
import com.example.gitobserverapp.utils.network.NetworkStatusHelper
import javax.inject.Inject

class MainFragment : Fragment(), RepoSearchAdapter.Listener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
//    private lateinit var networkStatus: InternetConnectionLiveData
    private lateinit var networkStatus: NetworkStatusHelper
    private val repoSearchAdapter: RepoSearchAdapter by lazy {
        RepoSearchAdapter(this)
    }

    @Inject lateinit var apiRepository: ApiRepository
    @Inject lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this@MainFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStatus = NetworkStatusHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderUi()
        recyclerViewInit()
        initViewStatement()
        startSearch()
    }

    private fun renderUi() {
        mainViewModel.reposLiveData.observe(viewLifecycleOwner){
            repoSearchAdapter.differ.submitList(it)
        }

        mainViewModel.mainNetworkLiveData.observe(viewLifecycleOwner){ isConnected ->
            if (isConnected) mainViewModel.setState(MainViewState.MainViewContentMain)
            else mainViewModel.setState(MainViewState.NetworkError)
        }

        networkStatus.observe(viewLifecycleOwner){ status ->
            if (status) {
                mainViewModel.setNetworkStatus(value = true)
            } else {
                mainViewModel.setNetworkStatus(value = false)
            }
        }
    }

    private fun startSearch() {
        binding.btnSearch.setOnClickListener {
            if (mainViewModel.mainNetworkLiveData.value == false) {
                mainViewModel.setState(MainViewState.NetworkError)
            } else {
                hideKeyboard(it)
                mainViewModel.setState(MainViewState.Loading)
                if (binding.edtTxtInput.text.isNullOrEmpty()) {
                    mainViewModel.setState(MainViewState.Error("Search field is empty"))
                    mainViewModel.setReposList(null)
                } else {
                    lifecycleScope.launchWhenCreated {
                        mainViewModel.getRepos(binding.edtTxtInput.text.toString(), 1)
                    }
                }
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
                is MainViewState.Error -> {
                    mainViewModel.setReposList(null)
                    binding.progBarMain.visibility = View.GONE
                    binding.networkError.root.visibility = View.GONE
                    binding.txtError.visibility = View.VISIBLE
                    binding.txtError.text = viewState.error
                    binding.btnSearch.isEnabled = true
                }
                is MainViewState.Loading -> {
                    binding.progBarMain.visibility = View.VISIBLE
                    binding.txtError.visibility = View.GONE
                    binding.networkError.root.visibility = View.GONE
                    binding.btnSearch.isEnabled = false
                }
                is MainViewState.MainViewContentMain -> {
                    binding.progBarMain.visibility = View.GONE
                    binding.networkError.root.visibility = View.GONE
                    binding.txtError.visibility = View.GONE
                    binding.btnSearch.isEnabled = true
                }
                is MainViewState.NetworkError -> {
                    mainViewModel.setReposList(null)
                    binding.progBarMain.visibility = View.GONE
                    binding.networkError.root.visibility = View.VISIBLE
                    binding.txtError.visibility = View.GONE
                    binding.btnSearch.isEnabled = false
                }
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
        val repoCreatedAt: String = item.created_at
        val repoOwnerLogin: String = item.owner.login
        val repoName: String = item.name
        val direction = MainFragmentDirections.actionMainFragmentToChartFragment(
            repoName = repoName,
            repoOwnerName = repoOwnerLogin,
            repoCreatedAt = repoCreatedAt
        )
        findNavController().navigate(directions = direction)
    }
}