package com.example.gitobserverapp.domain.model

data class DomainReposListModel(
    val items: List<Items>,
)

data class Items(
    val created_at: String,
    val repo_name: String,
    val stargazers_count: Int,
    val owner_avatar_url: String,
    val owner_login: String
)
