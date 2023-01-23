package com.example.gitobserverapp.data.network.model

data class ListStargazersModel(
    val data: ArrayList<StarredModelItem>
)

data class StarredModelItem(
    val starred_at: String,
    val user: User
)

data class User(
    val id: Int,
    val login: String,
    val avatar_url: String
)