package com.example.gitobserverapp.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gitobserverapp.domain.usecase.GetStargazersUseCase

class ChartViewModelFactory(private val getStargazersUseCase: GetStargazersUseCase): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChartViewModel(getStargazersUseCase = getStargazersUseCase) as T
    }
}