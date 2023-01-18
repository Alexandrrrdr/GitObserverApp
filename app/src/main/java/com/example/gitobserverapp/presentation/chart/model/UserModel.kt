package com.example.gitobserverapp.presentation.chart.model

import com.example.gitobserverapp.data.network.model.starred.User
import java.time.LocalDate

data class UserModel(
    val repoName: String,
    val user: User,
    val starredAt: LocalDate
)
