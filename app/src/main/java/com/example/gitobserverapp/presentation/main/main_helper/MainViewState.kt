package com.example.gitobserverapp.presentation.main.main_helper

import com.example.gitobserverapp.data.network.model.repo.Item

sealed class MainViewState{
    object Loading: MainViewState()
    data class Error(val error: String) : MainViewState()
    data class MainViewContentMain(val result: List<Item>): MainViewState()
}