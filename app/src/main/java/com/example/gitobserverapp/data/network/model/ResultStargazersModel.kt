package com.example.gitobserverapp.data.network.model

data class ResultStargazersModel(
    val data: ArrayList<StargazerModel>
)

data class StargazerModel(
    val starred_at: String,
    val user: User
)

data class User(
    val id: Int,
    val login: String,
    val avatar_url: String
)