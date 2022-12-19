package com.example.gitobserverapp.presentation.main

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.example.gitobserverapp.presentation.helper.ViewState

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
        renderUi()
        initViewStatement()

        binding.btnSearch.setOnClickListener {
            if (checkInternetConnection()) {
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

    private fun checkInternetConnection(): Boolean {
        val connectionManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectionManager.activeNetwork ?: return false
            val activeNetwork = connectionManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectionManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    override fun onClick(item: MainModel) {
        val repoId: Int = item.repoId
        val repoOwnerLogin: String = item.repoOwnerName
        val repoName: String = item.repoName
        val bundle =
            bundleOf("repo_id" to repoId, "repo_name" to repoName, "owner_name" to repoOwnerLogin)
        findNavController().navigate(R.id.action_mainFragment_to_chartFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}