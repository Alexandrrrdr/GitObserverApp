package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.data.remote.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepoResultList
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainSearchPresenter @Inject constructor(
    private val getReposUseCase: GetReposUseCase
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
            val repoResult: GitResponse<RemoteRepoResultList> =
                        getReposUseCase.getData(
                            repo_name = searchName,
                            page_number = page
                        )

            when (repoResult) {
                is GitResponse.Success -> {
                    withContext(Dispatchers.Main) {
                        viewState.showSuccess(repoResult.data!!.repoList)
                    }
                }
                is GitResponse.Error -> {
                    withContext(Dispatchers.Main) {
                        viewState.showError(error = repoResult.error.toString())
                    }
                }
                is GitResponse.Exception -> {
                    withContext(Dispatchers.Main) {
                        viewState.showNetworkError()
                    }
                }
            }
        }
    }
}