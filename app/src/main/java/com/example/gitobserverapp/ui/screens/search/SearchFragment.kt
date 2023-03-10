package com.example.gitobserverapp.ui.screens.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitobserverapp.App
import com.example.gitobserverapp.R
import com.example.gitobserverapp.data.utils.Constants.START_PAGE
import com.example.gitobserverapp.data.utils.Constants.ZERO_INDEX
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.screens.base.BaseFragment
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.utils.Extensions.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate), SearchView,
    SearchAdapter.ClickListener{

    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading: Boolean = false
    private var isLoadAvailable: Boolean = true
    private var currentPage: Int = START_PAGE

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

        layoutManager = LinearLayoutManager(requireContext())
        recyclerViewInit()
        pagingRepoList()

        binding.btnSearch.setOnClickListener {
            this.isLoading = true
            searchPresenter.loadData(userName = binding.edtTxtInput.text.toString(), pageNumber = START_PAGE)
            hideKeyboard()
        }

        binding.btnCheckAgain.setOnClickListener {
            this.isLoading = true
            searchPresenter.loadData(userName = binding.edtTxtInput.text.toString(), pageNumber = currentPage)
        }
    }

    private fun pagingRepoList() {
        binding.recView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val pastVisibleItem: Int = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItems = searchAdapter.adapterList.size
                val isLastIndexIsVisible = pastVisibleItem == totalItems -1

                if (!isLoading && isLastIndexIsVisible && isLoadAvailable) {
                    searchPresenter.loadData(
                        userName = binding.edtTxtInput.text.toString(),
                        pageNumber = currentPage
                    )
                } else if ((!isLoading && isLastIndexIsVisible && !isLoadAvailable)){
                    Snackbar.make(binding.root, "No more data on server", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun showLoading() {
        binding.progBarMain.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.btnSearch.isEnabled = false
        binding.btnCheckAgain.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showSuccess(list: List<UiRepo>, isLoading: Boolean, isLoadAvailable: Boolean, currentPage: Int) {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.txtError.visibility = View.GONE
        this.isLoading = isLoading
        this.isLoadAvailable = isLoadAvailable
        this.currentPage = currentPage
        binding.btnSearch.isEnabled = true
        binding.btnCheckAgain.isVisible = false
        if (currentPage > START_PAGE){
            searchAdapter.setListAdapter(list = list, isNewList = false)
            searchAdapter.notifyDataSetChanged()
        } else {
            searchAdapter.setListAdapter(list = list, isNewList = true)
            searchAdapter.notifyDataSetChanged()
        }
    }

    override fun showError(error: String, typeError: Int) {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.btnSearch.isEnabled = true
        binding.btnCheckAgain.isVisible = false
        when(typeError){
            ZERO_INDEX -> {
                searchAdapter.setListAdapter(list = emptyList(), isNewList = true)
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            }
            START_PAGE -> {
                searchAdapter.setListAdapter(list = emptyList(), isNewList = false)
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun showNetworkError() {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.btnSearch.isEnabled = false
        binding.btnCheckAgain.isVisible = true
        searchAdapter.setListAdapter(list = emptyList(), isNewList = false)
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
        binding.recView.layoutManager = layoutManager
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = searchAdapter
    }
}