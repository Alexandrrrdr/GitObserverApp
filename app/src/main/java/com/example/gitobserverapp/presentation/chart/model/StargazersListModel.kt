package com.example.gitobserverapp.presentation.chart.model

data class StargazersListModel(
    val stargazers_list: List<StargazerModel>
)

data class StargazerModel(
    val starred_at: String,
    val id: Int,
    val login: String,
    val avatar_url: String
)
