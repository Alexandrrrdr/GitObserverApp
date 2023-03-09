package com.example.gitobserverapp.ui.screens.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitobserverapp.App
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.utils.Constants.START_PAGE
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.screens.BaseFragment
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.utils.Extensions.hideKeyboard
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate), SearchView,
    SearchAdapter.ClickListener{

    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(clickListener = this)
    }

    @Inject lateinit var getReposUseCaseUseCase: GetReposUseCase

    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun provideMainSearchPresenter(): SearchPresenter {
        return SearchPresenter(getReposUseCase = getReposUseCaseUseCase)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this@SearchFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewInit()

        binding.btnSearch.setOnClickListener {
            searchPresenter.loadData(userName = binding.edtTxtInput.text.toString(), pageNumber = START_PAGE)
            hideKeyboard()
        }

        binding.btnCheckAgain.setOnClickListener {
            searchPresenter.loadData(userName = binding.edtTxtInput.text.toString(), pageNumber = START_PAGE)
        }
    }

    override fun showLoading() {
        binding.progBarMain.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.btnSearch.isEnabled = false
        binding.btnCheckAgain.isVisible = false
    }

    override fun showSuccess(list: List<UiRepo>) {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.txtError.visibility = View.GONE
        binding.btnSearch.isEnabled = true
        binding.btnCheckAgain.isVisible = false
        searchAdapter.differ.submitList(list)
    }

    override fun showError(error: String) {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.txtError.visibility = View.VISIBLE
        binding.txtError.text = error
        binding.btnSearch.isEnabled = true
        binding.btnCheckAgain.isVisible = false
        searchAdapter.differ.submitList(emptyList())
    }

    override fun showNetworkError() {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.btnSearch.isEnabled = false
        binding.btnCheckAgain.isVisible = true
        searchAdapter.differ.submitList(emptyList())
    }

    override fun onClick(item: UiRepo) {
        val direction = SearchFragmentDirections.actionMainFragmentToChartFragment(
            repoName = item.name,
            repoOwnerName = item.owner.login,
            starAmount = item.starUserAmount
        )
        requireActivity().findNavController(R.id.fragment_holder).navigate(directions = direction)
    }



    private fun recyclerViewInit() {
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = searchAdapter
    }
}