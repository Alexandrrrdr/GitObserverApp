package com.example.gitobserverapp.data.network.model.repo

data class GitHubRepoResult(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)