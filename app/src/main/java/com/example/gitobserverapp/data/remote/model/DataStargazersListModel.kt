package com.example.gitobserverapp.data.remote.model

data class DataStargazersListModel<T>(
    val items: List<T>
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