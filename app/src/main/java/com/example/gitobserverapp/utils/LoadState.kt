package com.example.gitobserverapp.utils

import com.example.gitobserverapp.data.network.model.repo.GitHubRepoResult

sealed class ViewState{
    object Loading: ViewState()
    data class Error(val error: String) : ViewState()
    data class ViewContent(val result: GitHubRepoResult): ViewState()
}