package com.example.gitobserverapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitobserverapp.data.repository.ApiRepository

class MainViewModelFactory(val apiRepository: ApiRepository, val name: String): ViewModelProvider.AndroidViewModelFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(apiRepository, name) as T
    }

}