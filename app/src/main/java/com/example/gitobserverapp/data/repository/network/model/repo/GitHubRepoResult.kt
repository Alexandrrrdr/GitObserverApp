package com.example.gitobserverapp.data.repository.network.model.repo

data class GitHubRepoResult(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)