package com.example.gitobserverapp.ui.details.model

data class UsersListDetails(
    val users: List<User>
)

data class User(
    val id: Int,
    val login: String,
    val avatar_url: String
)

