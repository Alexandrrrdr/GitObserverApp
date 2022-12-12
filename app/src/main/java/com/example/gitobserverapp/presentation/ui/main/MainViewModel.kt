package com.example.gitobserverapp.presentation.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.data.repository.network.RetrofitInstance
import com.example.gitobserverapp.data.repository.network.model.GitHubRepoResult
import com.example.gitobserverapp.utils.Constants.SORT_BY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(): ViewModel() {

    private var _reposLiveData: MutableLiveData<List<MainModel>> = MutableLiveData<List<MainModel>>()
    val reposLiveData: LiveData<List<MainModel>> get() = _reposLiveData

    fun setReposList(list: GitHubRepoResult){
        val reposItems: ArrayList<MainModel> = arrayListOf()
        var repoModel: MainModel? = null
        for (i in (0 until list.items.size)){
            repoModel = MainModel(
                repoName = list.items[i].name,
                repoId = list.items[i].id,
                repoImageUrl = list.items[i].owner.avatar_url,
                repoOwnerName = list.items[i].owner.login,
                repoStarAmount = list.items[i].stargazers_count,
                stargazers_url = list.items[i].stargazers_url,
                subscribers_url = list.items[i].subscribers_url,
                subscription_url = list.items[i].subscription_url
            )
            reposItems.add(repoModel)
        }
        _reposLiveData.postValue(reposItems)
    }

    fun getRepos(searchWord: String){

        val retrofit = RetrofitInstance.retrofitInstance.getRepos(searchWord, SORT_BY)
        retrofit.enqueue(object : Callback<GitHubRepoResult> {
            override fun onResponse(call: Call<GitHubRepoResult>, response: Response<GitHubRepoResult>) {
                when (response.code()) {
                    in 200..303 -> {
                        response.body()?.let { get_repos ->
                            setReposList(get_repos)
                        }
                    }
                    in 304..421 -> {
                        Log.d("mylog", "${response.code()}")
                    }
                    in 422..502 -> {
                        Log.d("mylog", "${response.code()}")
                    }
                    in 503..599 -> {
                        Log.d("mylog", "${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<GitHubRepoResult>, t: Throwable) {
                Log.d("mylog", t.message.toString())
            }
        })
    }
}