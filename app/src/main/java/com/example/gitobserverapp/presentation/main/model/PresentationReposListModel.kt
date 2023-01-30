package com.example.gitobserverapp.presentation.main.model

data class ReposListModel(
    val items: List<RepoItem>
)

data class RepoItem(
    val owner_id: Int,
    val created_at: String,
    val repo_name: String,
    val stargazers_count: Int,
    val owner_avatar_url: String,
    val owner_login: String
)