package com.example.gitobserverapp.data.network.model.repos

data class GitHubRepoResult(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)

data class Item(
    val created_at: String,
    val description: String,
    val id: Int,
    val language: String,
    val name: String,
    val owner: Owner,
    val stargazers_count: Int
)

data class Owner(
    val avatar_url: String,
    val id: Int,
    val login: String
)