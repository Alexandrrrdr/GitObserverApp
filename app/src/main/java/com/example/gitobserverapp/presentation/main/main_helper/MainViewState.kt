package com.example.gitobserverapp.presentation.main.main_helper

sealed class MainViewState{
    object Loading: MainViewState()
    data class Error(val error: String) : MainViewState()
    object NetworkError : MainViewState()
    object MainViewContentMain : MainViewState()
}