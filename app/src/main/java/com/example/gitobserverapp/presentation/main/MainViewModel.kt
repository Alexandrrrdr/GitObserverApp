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
import javax.inject.Inject


class MainViewModel: ViewModel() {

    private var _reposLiveData: MutableLiveData<ReposListModel> = MutableLiveData<ReposListModel>()
    val reposLiveData: LiveData<ReposListModel> get() = _reposLiveData

    private var _mainViewViewState: MutableLiveData<MainViewState> = MutableLiveData<MainViewState>()
    val viewStateLiveData get() = _mainViewViewState

    private var _mainNetworkLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val mainNetworkLiveData get() = _mainNetworkLiveData

    fun setNetworkStatus(value: Boolean){
        _mainNetworkLiveData.postValue(value)
    }

    fun setReposList(list: List<ReposListModel>?){
        if (list == null){
            _reposLiveData.postValue(ReposListModel(listOf()))
        }
    }

    fun getRepos(searchName: String, page: Int){
        viewModelScope.launch {
//            val domainReposList = getReposUseCase.getData(value_one = searchName, value_two = "", value_three = page)
//            _reposLiveData.postValue(DomainToPresentationReposListMapper().map(domainReposList))

        }
    }

//    fun getRepos(searchWord: String, page: Int){
//        _mainViewViewState.postValue(MainViewState.Loading)
//        viewModelScope.launch{
//            try {
//                val retrofit = repository.getRepositories(searchName = searchWord, page = page)
//                if (retrofit.isSuccessful && retrofit.body() != null){
//                    when (retrofit.code()) {
//                        in 200..303 -> {
//                            retrofit.body().let { get_repos ->
//                                _mainViewViewState.postValue(MainViewState.MainViewContentMain)
//                                _reposLiveData.postValue(value = get_repos.items)
//                            }
//                        }
//                        in 304..600 -> {
//                            _mainViewViewState.postValue(MainViewState.Error(retrofit.message().toString()))
//                        }
//                    }
//                }
//            } catch (e: Exception){
//                _mainNetworkLiveData.postValue(false)
//            }
//        }
//    }

    fun setState(mainViewState: MainViewState){
        _mainViewViewState.postValue(mainViewState)
    }
}
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
