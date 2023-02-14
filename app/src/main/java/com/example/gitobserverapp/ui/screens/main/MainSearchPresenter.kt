package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.domain.model.NetworkState
import com.example.gitobserverapp.domain.usecase.GetRepoUseCase
import com.example.gitobserverapp.utils.mapper.UiRepoMapper
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainSearchPresenter @Inject constructor(
    private val getRepoUseCase: GetRepoUseCase,
    private val uiRepoMapper: UiRepoMapper
) : MvpPresenter<MainSearchView>() {


    fun loadData(searchName: String, page: Int) {

        if (searchName.isEmpty()) {
            viewState.showError("Search field is empty")
            return
        }
        viewState.showLoading()

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val result = getRepoUseCase.getData(repo_name = searchName, page_number = page)
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
                    is NetworkState.Success -> viewState.showSuccess(uiRepoMapper.mapRepoListToUi(result.data))
                }
            }
        }
    }
}