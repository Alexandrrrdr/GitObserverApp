//package com.example.gitobserverapp.presentation.main
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.gitobserverapp.domain.usecase.GetReposUseCase
//
//class MainViewModelFactory(private val getReposUseCase: GetReposUseCase): ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MainViewModel(getReposUseCase = getReposUseCase) as T
//    }
//}