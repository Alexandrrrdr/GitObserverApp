package com.example.gitobserverapp.ui.screens.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gitobserverapp.App
import com.example.gitobserverapp.R
import com.example.gitobserverapp.databinding.FragmentMainBinding
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.screens.main.model.UiRepo
import com.example.gitobserverapp.utils.Extensions.hideKeyboard
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MainSearchFragment: MvpAppCompatFragment(), MainSearchView, MainSearchAdapters.Listener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainSearchAdapters: MainSearchAdapters by lazy {
        MainSearchAdapters(this)
    }

    @Inject lateinit var getReposUseCaseUseCase: GetReposUseCase

    @InjectPresenter
    lateinit var mainSearchPresenter: MainSearchPresenter

    @ProvidePresenter
    fun provideMainSearchPresenter(): MainSearchPresenter {
        return MainSearchPresenter(getReposUseCase = getReposUseCaseUseCase)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this@MainSearchFragment)
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

        binding.btnSearch.setOnClickListener {
            mainSearchPresenter.loadData(userName = binding.edtTxtInput.text.toString())
            hideKeyboard()
        }

        binding.btnCheckAgain.setOnClickListener {
            mainSearchPresenter.loadData(userName = binding.edtTxtInput.text.toString())
        }
    }

    override fun showLoading() {
        binding.progBarMain.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.btnSearch.isEnabled = false
        binding.btnCheckAgain.isVisible = false
        mainSearchAdapters.differ.submitList(emptyList())
    }

    override fun showSuccess(list: List<UiRepo>) {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.txtError.visibility = View.GONE
        binding.btnSearch.isEnabled = true
        binding.btnCheckAgain.isVisible = false
        mainSearchAdapters.differ.submitList(list)
    }

    override fun showError(error: String) {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.GONE
        binding.txtError.visibility = View.VISIBLE
        binding.txtError.text = error
        binding.btnSearch.isEnabled = true
        binding.btnCheckAgain.isVisible = false
        mainSearchAdapters.differ.submitList(emptyList())
    }

    override fun showNetworkError() {
        binding.progBarMain.visibility = View.GONE
        binding.networkError.root.visibility = View.VISIBLE
        binding.txtError.visibility = View.GONE
        binding.btnSearch.isEnabled = false
        binding.btnCheckAgain.isVisible = true
        mainSearchAdapters.differ.submitList(emptyList())
    }

    override fun onClick(item: UiRepo) {
        val repoOwnerLogin: String = item.owner.login
        val repoName: String = item.name
        val direction = MainSearchFragmentDirections.actionMainFragmentToChartFragment(
            repoName = repoName,
            repoOwnerName = repoOwnerLogin
        )
        requireActivity().findNavController(R.id.fragment_holder).navigate(directions = direction)
    }

    private fun recyclerViewInit() {
        binding.recView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = mainSearchAdapters
    }
}