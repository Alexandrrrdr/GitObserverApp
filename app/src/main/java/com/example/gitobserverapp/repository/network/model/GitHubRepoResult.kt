package com.example.gitobserverapp.repository.network.model

data class GitHubRepoResult(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)