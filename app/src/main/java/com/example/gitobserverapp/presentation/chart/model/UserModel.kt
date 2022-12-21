package com.example.gitobserverapp.presentation.chart.model

import com.example.gitobserverapp.data.network.model.starred.User
import java.time.LocalDate

data class UserModel(
    val repoName: String,
    val users: User,
    val starredAt: LocalDate,
    val createdAt: LocalDate
)
