package com.example.gitobserverapp.presentation.chart.model

data class SearchModel(
    val repoOwnerName: String,
    val repoName: String,
    val createdAt: String,
    val page: Int
)
