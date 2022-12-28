//package com.example.gitobserverapp.presentation.main
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.gitobserverapp.data.repository.ApiRepository
//import javax.inject.Inject
//
//class MainViewModelFactory @Inject constructor(private var apiRepository: ApiRepository): ViewModelProvider.AndroidViewModelFactory() {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MainViewModel(apiRepository) as T
//    }
//
//}