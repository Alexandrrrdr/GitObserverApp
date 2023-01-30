package com.example.gitobserverapp.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.presentation.main.model.RepoItem
import com.example.gitobserverapp.presentation.mapping.repos.DomainToPresentationReposListMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@InjectViewState
class MainSearchPresenter(
    private val getReposUseCase: GetReposUseCase
) : MvpPresenter<MainSearchView>() {


    fun loadData(searchName: String, page: Int){
        val reposList = mutableListOf<RepoItem>()
        if (searchName.isEmpty()) {
            viewState.showError("Search field is empty")
        }
        viewState.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val domainReposList =
                getReposUseCase.getData(repo_name = searchName, page_number = page)
            reposList.addAll(DomainToPresentationReposListMapper().map(domainReposList).items)
        }
        viewState.showSuccess(reposList)
    }

}