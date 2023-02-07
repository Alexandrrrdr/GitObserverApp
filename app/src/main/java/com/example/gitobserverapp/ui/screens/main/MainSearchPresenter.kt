package com.example.gitobserverapp.ui.screens.main

import com.example.gitobserverapp.data.remote.model.GitResponse
import com.example.gitobserverapp.data.remote.model.RemoteRepo
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MainSearchPresenter(
    private val getReposUseCase: GetReposUseCase
) : MvpPresenter<MainSearchView>() {


    fun loadData(searchName: String, page: Int) {

        var reposList = emptyList<RemoteRepo>()

        if (searchName.isEmpty()) {
            viewState.showError("Search field is empty")
            return
        }
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        viewState.showLoading()
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {


            val repoResult: GitResponse<List<RemoteRepo>> = async { getReposUseCase.getData(repo_name = searchName, page_number = page) }.await()


            when (repoResult) {
                is GitResponse.Success -> {
                    withContext(Dispatchers.Main) {
                        reposList = repoResult.data!!
                        viewState.showSuccess(reposList)
                    }
                }
                is GitResponse.Error -> {
                    withContext(Dispatchers.Main) {
                        viewState.showError(error = repoResult.message.toString())
                    }
                }
            }

//            val domainReposList = async { getReposUseCase.getData(repo_name = searchName, page_number = page) }.await()
//
//            if (domainReposList.) {
//                withContext(Dispatchers.Main){
//                    if (domainReposList.items.isNotEmpty()){
//                        reposList.addAll(DomainToPresentationReposListMapper().map(domainReposList).items)
//                        viewState.showSuccess(reposList)
//                    } else {
//                        viewState.showError("No data from server")
//                    }
//                }
//            } else {
//                withContext(Dispatchers.Main){
//                    viewState.showNetworkError()
//                }
//            }
        }
    }
}