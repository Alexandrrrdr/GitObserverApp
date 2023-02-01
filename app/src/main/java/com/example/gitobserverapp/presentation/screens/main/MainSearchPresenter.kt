package com.example.gitobserverapp.presentation.screens.main

import android.util.Log
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.presentation.mapping.repos.DomainToPresentationReposListMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            val domainReposList = withContext(Dispatchers.IO) {
                getReposUseCase.getData(repo_name = searchName, page_number = page)
            }
            if (domainReposList.hasNetwork) {
                withContext(Dispatchers.Main){
                    reposList.addAll(DomainToPresentationReposListMapper().map(domainReposList).items)
                    viewState.showSuccess(reposList)
                    Log.d("info", "Test request ${domainReposList.items.size}")
                }
            } else {
                withContext(Dispatchers.Main){
                    viewState.showError("No data from server")
                }
            }
        }
    }
}