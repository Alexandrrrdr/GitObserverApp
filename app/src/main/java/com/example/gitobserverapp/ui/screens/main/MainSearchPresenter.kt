package com.example.gitobserverapp.ui.screens.main

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainSearchPresenter @Inject constructor(
    private val getRepoUseCase: com.example.gitobserverapp.domain.usecase.GetRepoUseCase,
    private val uiMapper: UiMapper
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
            val result = uiMapper.map(getRepoUseCase.getData(repo_name = searchName, page_number = page))

            viewState.showSuccess(result.repoList)


//            when (repoResult) {
//                is com.example.gitobserverapp.data.remote.GitResponse.Success -> {
//                    withContext(Dispatchers.Main) {
//                        viewState.showSuccess(repoResult.data!!.repoList)
//                    }
//                }
//                is com.example.gitobserverapp.data.remote.GitResponse.Error -> {
//                    withContext(Dispatchers.Main) {
//                        viewState.showError(error = repoResult.error.toString())
//                    }
//                }
//                is com.example.gitobserverapp.data.remote.GitResponse.Exception -> {
//                    withContext(Dispatchers.Main) {
//                        viewState.showNetworkError()
//                    }
//                }
//            }
        }
    }
}