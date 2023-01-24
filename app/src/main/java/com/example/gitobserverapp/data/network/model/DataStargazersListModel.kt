package com.example.gitobserverapp.data.network.model

class DataStargazersListModel: ArrayList<DataStargazersListItem>()

data class DataStargazersListItem(
    val starred_at: String,
    val user: User
)

data class User(
    val id: Int,
    val login: String,
    val avatar_url: String
)