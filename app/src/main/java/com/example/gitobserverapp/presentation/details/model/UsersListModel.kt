package com.example.gitobserverapp.presentation.details.model

import com.example.gitobserverapp.data.network.model.stargazers.User

data class UsersListModel(
    val period: String,
    val users: List<User>
)
