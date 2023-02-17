package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.ui.screens.main.model.UiRepo
import com.example.gitobserverapp.ui.screens.main.model.UiRepoOwner
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
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val result = getReposUseCase.getRepos(userName = userName)
            withContext(Dispatchers.Main){
                when (result) {
                    is NetworkState.Error -> viewState.showError(result.error)
                    is NetworkState.HttpErrors.BadGateWay -> viewState.showError(result.exception)
                    is NetworkState.HttpErrors.InternalServerError -> viewState.showError(result.exception)
                    is NetworkState.HttpErrors.RemovedResourceFound -> viewState.showError(result.exception)
                    is NetworkState.HttpErrors.ResourceForbidden -> viewState.showError(result.exception)
                    is NetworkState.HttpErrors.ResourceNotFound -> viewState.showError(result.exception)
                    is NetworkState.HttpErrors.ResourceRemoved -> viewState.showError(result.exception)
                    NetworkState.InvalidData -> viewState.showError("No data on server")
                    is NetworkState.NetworkException -> viewState.showNetworkError()
                    is NetworkState.Success -> viewState.showSuccess(result.data.map { UiRepo(
                        id = it.id, description = it.description, name = it.name, owner =
                        UiRepoOwner(avatarUrl = it.owner.avatarUrl, id = it.owner.id, login = it.owner.login),
                        created = it.created, starUserAmount = it.starUserAmount
                    ) })
                }
            }
        }
    }
}