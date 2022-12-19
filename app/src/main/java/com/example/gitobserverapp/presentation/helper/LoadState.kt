package com.example.gitobserverapp.presentation.helper

import com.example.gitobserverapp.data.network.model.repo.GitHubRepoResult

sealed class ViewState{
    object Loading: ViewState()
    data class Error(val error: String) : ViewState()
    data class ViewContentMain(val result: GitHubRepoResult): ViewState()
}