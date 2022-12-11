package com.example.gitobserverapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.repository.network.RetrofitInstance
import com.example.gitobserverapp.repository.network.model.GitHubRepoResult
import com.example.gitobserverapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(): ViewModel() {

    private var _reposLiveData: MutableLiveData<GitHubRepoResult> = MutableLiveData<GitHubRepoResult>()
    val reposLiveData: LiveData<GitHubRepoResult> get() = _reposLiveData

    fun setReposList(list: GitHubRepoResult){
        _reposLiveData.postValue(list)
    }

    fun getRepos(searchWord: String){

        val retrofit = RetrofitInstance.retrofitInstance.getRepos(searchWord, Constants.SORT_BY_RATE)
        retrofit.enqueue(object : Callback<GitHubRepoResult> {
            override fun onResponse(call: Call<GitHubRepoResult>, response: Response<GitHubRepoResult>) {
                when (response.code()) {
                    in 200..303 -> {
                        response.body()?.let { get_repos ->
                            _reposLiveData.postValue(get_repos)
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