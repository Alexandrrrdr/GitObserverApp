package com.example.gitobserverapp.presentation.ui.main

data class MainModel(
    val repoName: String,
    val repoId: Int,
    val repoImageUrl: String,
    val repoOwnerName: String,
    val repoStarAmount: Int,
    val stargazers_url: String,
    val subscribers_url: String,
    val subscription_url: String
)
