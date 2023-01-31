package com.example.gitobserverapp.presentation.main_old.main_helper

sealed class MainViewState{
    object Loading: MainViewState()
    data class Error(val error: String) : MainViewState()
    object NetworkError : MainViewState()
    object MainViewContentMain : MainViewState()
}