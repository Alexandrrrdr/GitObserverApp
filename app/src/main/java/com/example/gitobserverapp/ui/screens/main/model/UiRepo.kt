package com.example.gitobserverapp.ui.screens.main.model

data class UiRepo(
    val id: Int,
    val name: String,
    val starsCount: Int,
    val owner: UiRepoUser
)

data class UiRepoUser(
    val id: Int,
    val login: String,
    val avatarUrl: String
)
