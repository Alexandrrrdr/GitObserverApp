package com.example.gitobserverapp.data.remote.model

data class DataReposListModel(
    val items: List<Item>,
    val total_count: Int
)
data class Item(
    val name: String,
    val owner: Owner,
    val stargazers_count: Int
)
data class Owner(
    val avatar_url: String,
    val login: String
)