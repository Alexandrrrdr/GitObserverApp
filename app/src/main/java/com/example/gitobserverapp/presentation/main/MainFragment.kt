package com.example.gitobserverapp.presentation.main

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.App
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.presentation.InternetConnection
import com.example.gitobserverapp.presentation.main.main_helper.ViewState
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainFragment : Fragment(), RepoSearchAdapter.Listener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val internet = InternetConnection()
    private val repoSearchAdapter: RepoSearchAdapter by lazy {
        RepoSearchAdapter(this)
    }

    @Inject lateinit var apiRepository: ApiRepository
    @Inject lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this@MainFragment)
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

        recyclerViewInit()
        initViewStatement()
        renderUi()
        checkInternet()
    }

    private fun renderUi() {
//        mainViewModel.reposLiveData.observe(viewLifecycleOwner){
//            repoSearchAdapter.differ.submitList(it)
//        }
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
                    mainViewModel.setSearchName(binding.edtTxtInput.text.toString())


                    lifecycleScope.launchWhenCreated {
                        mainViewModel.repoList.collect{ pagingData ->
                            repoSearchAdapter.submitData(pagingData)
//                            repoSearchAdapter.differ.submitList(it)
                        }
                    }
//                    lifecycleScope.launchWhenCreated {
//                        mainViewModel.getRepos(binding.edtTxtInput.text.toString())
//                    }
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
        mainViewModel.setSearchName(binding.edtTxtInput.text.toString())
        val direction = MainFragmentDirections.actionMainFragmentToChartFragment(
            repoName = repoName,
            repoOwnerName = repoOwnerLogin,
            repoCreatedAt = repoCreatedAt
        )
        findNavController().navigate(directions = direction)
    }
}