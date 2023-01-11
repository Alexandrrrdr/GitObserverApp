package com.example.gitobserverapp.presentation.details.model

import com.example.gitobserverapp.data.network.model.starred.User

data class UserData(
    val period: String,
    val users: List<User>
)
