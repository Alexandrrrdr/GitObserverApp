package com.example.gitobserverapp.data.network.model

data class DataReposListModel(
    val items: List<Item>,
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