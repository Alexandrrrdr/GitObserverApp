package com.example.gitobserverapp.presentation.main.main_helper

import com.example.gitobserverapp.data.network.model.repo.Item

sealed class ViewState{
    object Loading: ViewState()
    data class Error(val error: String) : ViewState()
    data class ViewContentMain(val result: List<Item>): ViewState()
}