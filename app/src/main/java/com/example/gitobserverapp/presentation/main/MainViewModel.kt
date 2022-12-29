package com.example.gitobserverapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.presentation.main.main_helper.MainViewState
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    private var _reposLiveData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
    val reposLiveData: LiveData<List<Item>> get() = _reposLiveData

    private var _mainViewViewState: MutableLiveData<MainViewState> = MutableLiveData<MainViewState>()
    val viewStateLiveData get() = _mainViewViewState

    fun setReposList(list: List<Item>?){
        if (list == null){
            _reposLiveData.postValue(listOf())
        }
    }

    fun getRepos(searchWord: String, page: Int){
        _mainViewViewState.postValue(MainViewState.Loading)
        viewModelScope.launch{
            val retrofit = apiRepository.getRepositories(searchName = searchWord, page = page)
            if (retrofit.isSuccessful && retrofit.body() != null){
                when (retrofit.code()) {
                    in 200..303 -> {
                        retrofit.body().let { get_repos ->
                            _mainViewViewState.postValue(MainViewState.MainViewContentMain(get_repos!!.items))
                            _reposLiveData.postValue(get_repos.items)
                        }
                    }
                    in 304..600 -> {
                        _mainViewViewState.postValue(MainViewState.Error(retrofit.message().toString()))
                    }
                }
            }
            _mainViewViewState.postValue(MainViewState.Error("No data from server"))
        }
    }

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
