package com.example.gitobserverapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.domain.usecase.GetReposUseCase
import com.example.gitobserverapp.presentation.main.main_helper.MainViewState
import com.example.gitobserverapp.presentation.main.model.ReposListModel
import com.example.gitobserverapp.presentation.mapping.repos.DomainToPresentationReposListMapper
import kotlinx.coroutines.launch

class MainViewModel(private val getReposUseCase: GetReposUseCase): ViewModel() {

    private var _reposLiveData: MutableLiveData<ReposListModel> = MutableLiveData<ReposListModel>()
    val reposLiveData: LiveData<ReposListModel> get() = _reposLiveData

    private var _mainViewViewState: MutableLiveData<MainViewState> = MutableLiveData<MainViewState>()
    val viewStateLiveData get() = _mainViewViewState

    fun setReposList(list: List<ReposListModel>?){
        if (list == null){
            _reposLiveData.postValue(ReposListModel(listOf()))
        }
    }

    fun getRepos(searchName: String, page: Int){
        viewModelScope.launch {
            val domainReposList = getReposUseCase.getData(repo_name = searchName, page_number = page)
            _reposLiveData.postValue(DomainToPresentationReposListMapper().map(domainReposList))
        }
    }

    fun setState(mainViewState: MainViewState){
        _mainViewViewState.postValue(mainViewState)
    }
}

        //TODO Pagination for main fragment
//    val repoList: StateFlow<PagingData<Item>> = query
//        .map(::newPager)
//        .flatMapLatest { pager -> pager.flow }
//        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
//
//    private fun newPager(query: String): Pager<Int, Item> {
//        return Pager(PagingConfig(30, prefetchDistance = 5, enablePlaceholders = false)) {
//            RepoPagingSource(apiRepository = apiRepository, query = query)
//        }
//    }

//    val repoList = Pager(PagingConfig(1)){
//        RepoPagingSource(apiRepository = apiRepository, searchName = )
//    }.flow.cachedIn(viewModelScope)

    //    private val _query = MutableStateFlow("")
//    private val query: StateFlow<String> = _query.asStateFlow()
//
//    fun setSearchName(searchName: String){
//        _query.tryEmit(searchName)
//    }
