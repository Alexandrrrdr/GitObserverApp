package com.example.gitobserverapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitobserverapp.data.network.model.repo.Item
import com.example.gitobserverapp.data.repository.ApiRepository
import com.example.gitobserverapp.utils.Constants.SORT_BY
import com.example.gitobserverapp.presentation.main.main_helper.ViewState
import com.example.gitobserverapp.utils.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    private var _reposLiveData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
    val reposLiveData: LiveData<List<Item>> get() = _reposLiveData

    private var _viewStateLiveData: MutableLiveData<ViewState> = MutableLiveData<ViewState>()
    val viewStateLiveData get() = _viewStateLiveData

    fun setReposList(list: List<Item>?){
        if (list == null){
            _reposLiveData.postValue(listOf())
        }
    }

    fun getRepos(searchWord: String){
        _viewStateLiveData.postValue(ViewState.Loading)
        viewModelScope.launch{
//            val retrofit = RetrofitInstance.retrofitInstance.getRepos(searchWord, SORT_BY, Constants.PER_PAGE)
            val retrofit = apiRepository.getRepositories(searchWord)

            if (retrofit.isSuccessful && retrofit.body() != null){
                when (retrofit.code()) {
                    in 200..303 -> {
                        retrofit.body().let { get_repos ->
                            _viewStateLiveData.postValue(ViewState.ViewContentMain(get_repos!!.items))
                            _reposLiveData.postValue(get_repos.items)
                        }
                    }
                    in 304..600 -> {
                        _viewStateLiveData.postValue(ViewState.Error(retrofit.message().toString()))
                    }
                }
            }
        }
    }

    fun setStatement(error: String){
        _viewStateLiveData.postValue(ViewState.Error(error = error))
    }
}