package com.example.gitobserverapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitobserverapp.api.model.GetRepos

class MainViewModel(): ViewModel() {

    private var _reposLiveData: MutableLiveData<GetRepos> = MutableLiveData<GetRepos>()
    val reposLiveData: LiveData<GetRepos> get() = _reposLiveData

    fun getRepositories(reposName: String){

    }
}