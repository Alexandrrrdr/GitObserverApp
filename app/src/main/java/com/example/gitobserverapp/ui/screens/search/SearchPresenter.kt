package com.example.gitobserverapp.ui.screens.search

import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.screens.search.model.UiRepo
import com.example.gitobserverapp.ui.screens.search.model.UiRepoOwner
import com.example.gitobserverapp.utils.Constants.MAX_REPOS_PER_PAGE
import com.example.gitobserverapp.utils.Constants.MAX_STARS_PER_PAGE
import com.example.gitobserverapp.utils.Constants.START_PAGE
import com.example.gitobserverapp.utils.Constants.ZERO_INDEX
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val getReposUseCase: GetReposUseCase
) : MvpPresenter<SearchView>() {

    private var currentPage = 0
    fun loadData(userName: String, pageNumber: Int) {

        if (userName.isEmpty()) {
            viewState.showError("Search field is empty", ZERO_INDEX)
            return
        }
        viewState.showLoading()
        this.currentPage = pageNumber
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getReposUseCase.getRepos(repoName = userName, pageNumber = pageNumber)
                if (result.isNotEmpty()){
                    currentPage++
                    withContext(Dispatchers.Main){
                        viewState.showSuccess(result.map { UiRepo(
                            id = it.id,
                            description = it.description,
                            name = it.name,
                            owner = UiRepoOwner(avatarUrl = it.owner.avatarUrl, id = it.owner.id, login = it.owner.login),
                            created = it.created,
                            starUserAmount = it.starUserAmount
                        ) }, isLoadAvailable = result.size == MAX_REPOS_PER_PAGE,
                        currentPage = currentPage,
                        isLoading = false)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        viewState.showError("No data", START_PAGE)
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