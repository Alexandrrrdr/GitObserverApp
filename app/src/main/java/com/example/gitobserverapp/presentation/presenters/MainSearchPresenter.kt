package com.example.gitobserverapp.presentation.presenters

import android.util.Log
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.presentation.main.model.RepoItem
import com.example.gitobserverapp.presentation.mapping.repos.DomainToPresentationReposListMapper
import com.example.gitobserverapp.presentation.views.MainSearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MainSearchPresenter(
    private val getReposUseCase: GetReposUseCase
) : MvpPresenter<MainSearchView>() {


    fun loadData(searchName: String, page: Int) {
        val reposList = mutableListOf<RepoItem>()

        if (searchName.isEmpty()) {
            viewState.showError("Search field is empty")
            return
        }
        viewState.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val domainReposList =
                getReposUseCase.getData(repo_name = searchName, page_number = page)
            Log.d("info", "Test request ${domainReposList.items.size}")

            reposList.addAll(DomainToPresentationReposListMapper().map(domainReposList).items)
        }
        viewState.showSuccess(reposList)

    }
}