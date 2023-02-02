package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.mapping.repos.DomainToPresentationReposListMapper
import kotlinx.coroutines.*
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
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }

        viewState.showLoading()
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val domainReposList = withContext(Dispatchers.IO) {
                getReposUseCase.getData(repo_name = searchName, page_number = page)
            }
            if (domainReposList.hasNetwork) {
                withContext(Dispatchers.Main){
                    reposList.addAll(DomainToPresentationReposListMapper().map(domainReposList).items)
                    viewState.showSuccess(reposList)
                }
            } else {
                withContext(Dispatchers.Main){
                    viewState.showError("No data from server")
                }
            }
        }
    }
}