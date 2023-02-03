package com.example.gitobserverapp.data.network.model

data class DataStargazersListModel(
    val items: List<DataStargazersListItem>
)

data class DataStargazersListItem(
    val starred_at: String,
    val user: UserOld
)

data class UserOld(
    val id: Int,
    val login: String,
    val avatar_url: String
)