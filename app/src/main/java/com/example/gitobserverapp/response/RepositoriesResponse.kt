package com.example.gitobserverapp.response

data class RepositoriesResponse(
    val repoName: String? = null,
    val repoDescription: String? = null,
    val repoRate: Int = 0,
    val repoImageUrl: String? = null
)