package com.example.gitobserverapp.presentation.main.model

data class ReposListModel(
    val items: List<RepoItem>
)

data class Item(
    val created_at: String,
    val name: String,
    val owner: Owner,
    val stargazers_count: Int
)

data class Owner(
    val avatar_url: String,
    val id: Int,
    val login: String
)

data class RepoItem(
    val created_at: String,
    val repo_name: String,
    val stargazers_count: Int,
    val owner_avatar_url: String,
    val owner_login: String
)