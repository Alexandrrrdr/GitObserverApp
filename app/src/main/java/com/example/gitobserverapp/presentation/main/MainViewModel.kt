package com.example.gitobserverapp.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.network.RetrofitInstance
import com.example.gitobserverapp.data.network.model.repo.GitHubRepoResult
import com.example.gitobserverapp.utils.Constants.SORT_BY
import com.example.gitobserverapp.utils.ViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(): ViewModel() {

    private var _reposLiveData: MutableLiveData<List<MainModel>> = MutableLiveData<List<MainModel>>()
    val reposLiveData: LiveData<List<MainModel>> get() = _reposLiveData

    private var _viewStateLiveData: MutableLiveData<ViewState> = MutableLiveData<ViewState>()
    val viewStateLiveData get() = _viewStateLiveData

    fun setReposList(list: GitHubRepoResult?){
        if (list == null){
            _reposLiveData.postValue(listOf())
        } else {
            val reposItems: ArrayList<MainModel> = arrayListOf()
            var repoModel: MainModel?
            for (i in (0 until list.items.size)){
                repoModel = MainModel(
                    repoName = list.items[i].name,
                    repoId = list.items[i].id,
                    repoImageUrl = list.items[i].owner.avatar_url,
                    repoOwnerName = list.items[i].owner.login,
                    repoStarAmount = list.items[i].stargazers_count,
                    subscribers_url = list.items[i].subscribers_url,
                    repoCreated = list.items[i].created_at
                )
                reposItems.add(repoModel)
            }
            _reposLiveData.postValue(reposItems)
        }
    }

    fun getRepos(searchWord: String){
        _viewStateLiveData.postValue(ViewState.Loading)
        val retrofit = RetrofitInstance.retrofitInstance.getRepos(searchWord, SORT_BY)
        retrofit.enqueue(object : Callback<GitHubRepoResult> {
            override fun onResponse(call: Call<GitHubRepoResult>, response: Response<GitHubRepoResult>) {
                when (response.code()) {
                    in 200..303 -> {
                        response.body()?.let { get_repos ->
                            _viewStateLiveData.postValue(ViewState.ViewContent(get_repos))
                        }
                    }
                    in 304..421 -> {
                        _viewStateLiveData.postValue(ViewState.Error(response.message().toString()))
                    }
                    in 422..502 -> {
                        _viewStateLiveData.postValue(ViewState.Error(response.message().toString()))
                    }
                    in 503..599 -> {
                        _viewStateLiveData.postValue(ViewState.Error(response.message().toString()))
                    }
                }
            }
            override fun onFailure(call: Call<GitHubRepoResult>, t: Throwable) {
                Log.d("mylog", t.message.toString())
            }
        })
    }

    fun setStatement(error: String){
        _viewStateLiveData.postValue(ViewState.Error(error = error))
    }
}