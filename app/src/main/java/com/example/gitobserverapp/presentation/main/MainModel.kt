package com.example.gitobserverapp.presentation.main

data class MainModel(
    val repoName: String,
    val repoId: Int,
    val repoImageUrl: String,
    val repoOwnerName: String,
    val repoStarAmount: Int,
    val subscribers_url: String,
    val repoCreated: String
)
