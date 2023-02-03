package com.example.gitobserverapp.data.remote.model

data class DataReposListModel<ITEM>(
    val items: List<ITEM>,
    val total_count: Int
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