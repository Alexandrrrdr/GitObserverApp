package com.example.gitobserverapp.ui.screens.main.model

import com.squareup.moshi.Json

data class UiRepoResultList(
    val repoList: List<UiRepo>
)

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
