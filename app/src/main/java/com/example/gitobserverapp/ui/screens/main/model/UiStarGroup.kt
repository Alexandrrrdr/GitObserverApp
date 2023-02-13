package com.example.gitobserverapp.ui.screens.main.model

import java.util.*

data class UiStarGroup(
    val date: Date,
    val users: UiStarUser
)

data class UiStarUser(
    val id: Int,
    val name: String,
    val userAvaUrl: String
)