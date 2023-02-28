package com.example.gitobserverapp.ui.screens.search

import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.ui.screens.search.model.UiRepoOwner
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val getReposUseCase: GetReposUseCase
) : MvpPresenter<SearchView>() {


    fun loadData(userName: String) {

        if (userName.isEmpty()) {
            viewState.showError("Search field is empty")
            return
        }
        viewState.showLoading()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getReposUseCase.getRepos(userName = userName)
                if (result.isNotEmpty()){
                    withContext(Dispatchers.Main){
                        viewState.showSuccess(result.map { UiRepo(
                            id = it.id,
                            description = it.description,
                            name = it.name,
                            owner = UiRepoOwner(avatarUrl = it.owner.avatarUrl, id = it.owner.id, login = it.owner.login),
                            created = it.created,
                            starUserAmount = it.starUserAmount
                        ) })
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        viewState.showError("No data on server")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    viewState.showNetworkError()
                }
            }
        }
    }
}