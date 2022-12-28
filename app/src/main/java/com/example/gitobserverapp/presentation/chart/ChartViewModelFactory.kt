//package com.example.gitobserverapp.presentation.chart
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.gitobserverapp.data.repository.ApiRepository
//import javax.inject.Inject
//
//class ChartViewModelFactory @Inject constructor(private val apiRepository: ApiRepository): ViewModelProvider.AndroidViewModelFactory() {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return ChartViewModel(apiRepository = apiRepository) as T
//    }
//}